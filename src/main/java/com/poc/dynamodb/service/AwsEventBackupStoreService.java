package com.poc.dynamodb.service;

import com.poc.dynamodb.ApplicationContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;
import software.amazon.awssdk.utils.CollectionUtils;

public class AwsEventBackupStoreService {
    private static final long MAX_RETRY_COUNT = 5;
    ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(5);

    public void storeEventsPayloadInDB(
            List<WriteRequest> writeRequests,
            ExecutorService executorService,
            int processingCount) {
        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
        requestItems.put(ApplicationContext.getData().getTableName(), writeRequests);
        BatchWriteItemRequest batchWriteItem =
                BatchWriteItemRequest.builder().requestItems(requestItems).build();
        computeAndRetry(batchWriteItem, executorService, 0, processingCount);
    }

    private void computeAndRetry(
            BatchWriteItemRequest request,
            ExecutorService executorService,
            int retryCount,
            int processingCount) {
        DynamoDbAsyncClient dynamoDbAsyncClient = ApplicationContext.getClient().get();
        dynamoDbAsyncClient
                .batchWriteItem(request)
                .whenCompleteAsync(
                        (data, ex) -> {
                            if (ex != null) {
                                ex.printStackTrace();
                                System.err.println("Error while processing batch write");
                                System.err.println("Error batch - " + processingCount);
                            }
                            if (data == null) {
                                return;
                            }
                            if (CollectionUtils.isNullOrEmpty(data.unprocessedItems())) {
                                System.out.println("Success!!! No unprocessed items found");
                                System.out.println(
                                        "Success batch - "
                                                + processingCount
                                                + ", total count - "
                                                + processingCount * 25);
                                return;
                            }
                            if (retryCount < MAX_RETRY_COUNT) {
                                System.out.println("Retrieving the unprocessed items");
                                delayedRetry(data, executorService, retryCount);
                            } else {
                                System.err.println("Failed to store it in dynamoDB");
                                System.out.println("Failed batch after retry - " + processingCount);
                            }
                        },
                        executorService);
    }

    private void delayedRetry(
            BatchWriteItemResponse data, ExecutorService executorService, int retryCount) {
        System.out.println("Retrieving the unprocessed items");
        BatchWriteItemRequest batchWriteItemRequest =
                BatchWriteItemRequest.builder().requestItems(data.unprocessedItems()).build();
        long exponentialTime = getWaitTimeExp(retryCount);
        // Retry after wait time
        scheduledExecutor.schedule(
                () -> computeAndRetry(batchWriteItemRequest, executorService, 0, retryCount + 1),
                exponentialTime,
                TimeUnit.MILLISECONDS);
    }

    private long getWaitTimeExp(int retryCount) {
        if (0 == retryCount) {
            return 10L; // Minimum delay time is 10ms
        }
        return (long) Math.pow(2, retryCount) * 100L;
    }

    private static class AwsEventBackupStoreServiceSingleton {
        private static final AwsEventBackupStoreService INSTANCE = new AwsEventBackupStoreService();
    }

    public static AwsEventBackupStoreService getInstance() {
        return AwsEventBackupStoreService.AwsEventBackupStoreServiceSingleton.INSTANCE;
    }
}

package com.poc.dynamodb.processor.impl;

import com.poc.dynamodb.ApplicationContext;
import com.poc.dynamodb.model.Column;
import com.poc.dynamodb.model.Data;
import com.poc.dynamodb.processor.Processor;
import com.poc.dynamodb.service.AwsEventBackupStoreService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

public class BatchWriteProcessor implements Processor {

    private static final int BATCH_SIZE = 25;
    private final ExecutorService executorService;
    private final AwsEventBackupStoreService awsEventBackupStoreService;

    public BatchWriteProcessor() {
        executorService = Executors.newFixedThreadPool(25);
        awsEventBackupStoreService = AwsEventBackupStoreService.getInstance();
    }

    @Override
    public void execute() {
        System.out.println("Process Started");
        Data data = ApplicationContext.getData();
        int rowCount = data.getRowCount();
        int iterationCount = (rowCount + BATCH_SIZE - 1) / BATCH_SIZE;
        AwsEventBackupStoreService awsEventBackupStoreService =
                AwsEventBackupStoreService.getInstance();
        for (int i = 0; i < iterationCount; i++) {
            List<WriteRequest> batch = createBatch(data.getColumns());
            int finalI = i;
            CompletableFuture.runAsync(
                    () ->
                            awsEventBackupStoreService.storeEventsPayloadInDB(
                                    batch, executorService, finalI),
                    executorService);
            System.out.println("Initiated batch - " + i + ", total count " + i * 25);

            if (0 == i % 50) {
                try {
                    System.out.println("Wait for 5 sec...");
                    TimeUnit.SECONDS.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<WriteRequest> createBatch(List<Column> columns) {
        List<WriteRequest> writeRequests = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            writeRequests.add(Processor.build(columns));
        }
        return writeRequests;
    }
}

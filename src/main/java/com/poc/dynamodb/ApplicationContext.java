package com.poc.dynamodb;

import com.poc.dynamodb.config.DynamoDBClient;
import com.poc.dynamodb.model.Data;

public class ApplicationContext {

    private ApplicationContext() {}

    private static Data data;
    private static DynamoDBClient client;

    public static void init(Data data) {
        ApplicationContext.data = data;
        ApplicationContext.client =
                new DynamoDBClient(
                        data.getAwsAccessKey(),
                        data.getAwsSecretKey(),
                        data.getAwsProfile(),
                        data.getRegion());
    }

    public static Data getData() {
        return data;
    }

    public static DynamoDBClient getClient() {
        return client;
    }
}

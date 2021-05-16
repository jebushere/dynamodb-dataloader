package com.poc.dynamodb.config;

import static software.amazon.awssdk.utils.StringUtils.isNotBlank;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

public class DynamoDBClient {

    private final DynamoDbAsyncClient client;

    public DynamoDBClient(String accessKey, String secretKey, String awsProfile, Region region) {
        assert isNotBlank(accessKey) && isNotBlank(secretKey) && region != null;
        DynamoDbAsyncClientBuilder clientBuilder = DynamoDbAsyncClient.builder();
        if (isNotBlank(accessKey) && isNotBlank(secretKey)) {
            clientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)));
        } else if (isNotBlank(awsProfile)) {
            clientBuilder.credentialsProvider(
                    ProfileCredentialsProvider.builder().profileName(awsProfile).build());
        }
        client = clientBuilder.build();
    }

    public DynamoDbAsyncClient get() {
        return client;
    }
}

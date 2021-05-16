package com.poc.dynamodb.processor;

import com.poc.dynamodb.model.Column;
import com.poc.dynamodb.model.enumeration.EColumnType;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@FunctionalInterface
public interface Processor {
    void execute();

    AtomicInteger count = new AtomicInteger(1);

    static WriteRequest build(List<Column> columns) {
        Map<String, AttributeValue> item = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            item.put(column.getName(), getAttribute(column.getType(), column.getName(), 0 == i));
        }
        return WriteRequest.builder().putRequest(PutRequest.builder().item(item).build()).build();
    }

    private static AttributeValue getAttribute(
            EColumnType type, String columnName, boolean isCompositeKey) {
        AttributeValue.Builder builder = AttributeValue.builder();
        if (isCompositeKey) {
            if (type == EColumnType.N) {
                builder.n(Integer.toString(count.getAndIncrement()));
            } else {
                builder.s(UUID.randomUUID().toString());
            }
        } else if (type == EColumnType.N) {
            builder.n(Long.toString(Instant.now().getEpochSecond() + 10000L));
        } else {
            builder.s(String.join(columnName, "-", Double.toString(Math.random())));
        }
        return builder.build();
    }
}

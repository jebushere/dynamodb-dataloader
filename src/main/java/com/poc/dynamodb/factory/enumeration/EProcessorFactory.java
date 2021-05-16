package com.poc.dynamodb.factory.enumeration;

import com.poc.dynamodb.common.enumeration.EnumFI;

public enum EProcessorFactory implements EnumFI {
    BATCH_WRITE("BATCH_WRITE", "BATCH_WRITE");

    private final String key;
    private final String value;

    EProcessorFactory(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}

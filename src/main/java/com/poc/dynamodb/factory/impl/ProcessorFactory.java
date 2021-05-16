package com.poc.dynamodb.factory.impl;

import com.poc.dynamodb.factory.Factory;
import com.poc.dynamodb.factory.enumeration.EProcessorFactory;
import com.poc.dynamodb.processor.Processor;
import com.poc.dynamodb.processor.impl.BatchWriteProcessor;

public class ProcessorFactory implements Factory<Processor, EProcessorFactory> {

    private ProcessorFactory() {
        // Do nothing
    }

    @Override
    public Processor get(EProcessorFactory type) {
        assert type != null;
        switch (type) {
            case BATCH_WRITE:
                return new BatchWriteProcessor();
            default:
                throw new RuntimeException("Processor not found");
        }
    }

    private static class ProcessorFactorySingleton {
        private static final ProcessorFactory INSTANCE = new ProcessorFactory();
    }

    public static ProcessorFactory getInstance() {
        return ProcessorFactorySingleton.INSTANCE;
    }
}

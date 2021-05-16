package com.poc.dynamodb.factory;

import com.poc.dynamodb.common.enumeration.EnumFI;

@FunctionalInterface
public interface Factory<S, K extends EnumFI> {
    public S get(K type);
}

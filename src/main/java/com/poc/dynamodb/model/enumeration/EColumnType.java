package com.poc.dynamodb.model.enumeration;

public enum EColumnType {
    N("Number"),
    S("String");

    private final String name;

    EColumnType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.poc.dynamodb.model;

import com.poc.dynamodb.model.enumeration.EColumnType;

public class Column {
    private String name;
    private EColumnType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EColumnType getType() {
        return type;
    }

    public void setType(EColumnType type) {
        this.type = type;
    }
}

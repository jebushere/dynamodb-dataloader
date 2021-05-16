package com.poc.dynamodb.model;

import java.util.ArrayList;
import java.util.List;
import software.amazon.awssdk.regions.Region;

public class Data {

    private String process;
    private String tableName;
    private int rowCount;
    private List<Column> columns = new ArrayList<>();
    private String awsAccessKey;
    private String awsSecretKey;
    private String awsProfile;
    private Region region;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    public String getAwsProfile() {
        return awsProfile;
    }

    public void setAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = Region.of(region);
    }
}

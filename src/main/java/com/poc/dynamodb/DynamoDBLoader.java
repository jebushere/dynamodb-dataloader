package com.poc.dynamodb;

import static software.amazon.awssdk.utils.StringUtils.isBlank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.dynamodb.factory.enumeration.EProcessorFactory;
import com.poc.dynamodb.factory.impl.ProcessorFactory;
import com.poc.dynamodb.model.Data;
import com.poc.dynamodb.processor.Processor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DynamoDBLoader {

    public static void main(String[] args) {
        System.out.println("Application started");
        String path = args.length > 0 ? args[0] : "";
        InputStream jsonStream = null;
        try {
            if (isBlank(path)) {
                jsonStream = new FileInputStream("./application.json");
            } else {
                jsonStream = new FileInputStream(path);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (jsonStream == null) {
            System.err.println("File not found");
            System.exit(500);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Data data = mapper.readValue(jsonStream, Data.class);
            ApplicationContext.init(data);
        } catch (IOException e) {
            System.err.println("Error while parsing json");
            e.printStackTrace();
            System.exit(500);
        }
        Processor processor = ProcessorFactory.getInstance().get(EProcessorFactory.BATCH_WRITE);
        System.out.println("Process Started");
        processor.execute();
        System.out.println("Application completed");
    }
}

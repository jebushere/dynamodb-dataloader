# dynamodb-dataloader
AWS DynamoDb data loader Java application to load(insert) the data to table.

##Installation
- Check sample.json
- tableName, rowCount, columns and region are required field.
- For AWS authentication whether use, awsAccessKey or awsSecretKey combination or awsProfile or leave all the thre field empty. In case all the fields are empty, it will take the aws credential from the home path.
- columns - Atleast one object should be given in the columns array.
- First column will be considered as partition key.

###Sample.json
```json
{
"process": "write",
"tableName": "events_backup",
"rowCount": "100",
"awsAccessKey": "optional",
"awsSecretKey": "optional",
"region": "us-west-2",
"awsProfile": "default-optional",
"columns": [{
    "name": "eventUUID",
    "type": "S"
    },{
    "name": "event",
    "type": "S"
    },{
    "name": "expirationTime",
    "type": "N"
    },{
    "name": "producerClientId",
    "type": "S"
    }]
}
```

## Run application

### IDE
While running in the IDE provide the proper file path as the argument.

### Command line

Pass the first parameter as the file path of the configuration json. 
If not it will take the current path with the file name as `./application.json`.

Goto project home path where `dynamodb.jar` file is available.

Run the below command.

    java -jar dynamodb.jar
or with file path as argument like

    java -jar dynamodb.jar <file_path>

package org.alexp.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Table definition of a user of the app.
 */
@Getter
@Setter
@DynamoDBTable(tableName = "users")
public class User {

    @DynamoDBHashKey(attributeName = "email")
    String email;

    @DynamoDBAttribute(attributeName = "hashedPassword")
    String hashedPassword;

    @DynamoDBIgnore
    String token;
}

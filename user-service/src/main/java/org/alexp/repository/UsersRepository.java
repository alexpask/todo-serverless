package org.alexp.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.alexp.model.User;

public class UsersRepository {

    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    public UsersRepository() {
        client = AmazonDynamoDBClientBuilder
                .standard()
                .build();

        mapper = new DynamoDBMapper(client);
    }

    /**
     * Saves a user to the repository.
     *
     * @param user User object to save
     * @return
     */
    public User save(User user) {

        mapper.save(user);

        return user;
    }

    public User get(String id) {

        return  mapper.load(User.class, id);
    }
}

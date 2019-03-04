package org.alexp.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import org.alexp.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoRepository {

    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    public TodoRepository() {
        client = AmazonDynamoDBClientBuilder
                .standard()
                .build();

        mapper = new DynamoDBMapper(client);
    }

    public Todo save(Todo todo) {

        mapper.save(todo);

        return todo;
    }

    public Todo get(String userId, String todoId) {

        return mapper.load(Todo.class, userId, todoId);
    }

    public List<Todo> list(String userId) {

        DynamoDBQueryExpression<Todo> query =
                new DynamoDBQueryExpression<>();

        query.setHashKeyValues(Todo
                .builder()
                .userId(userId)
                .build());

        PaginatedQueryList<Todo> todos =
                mapper.query(Todo.class, query);

        todos.loadAllResults();

        return new ArrayList<>(todos);
    }
}


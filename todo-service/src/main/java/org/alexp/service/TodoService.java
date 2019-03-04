package org.alexp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexp.model.AddTodo;
import org.alexp.model.Todo;
import org.alexp.repository.TodoRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TodoService {

    private final ObjectMapper mapper;
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {

        this.mapper = new ObjectMapper();
        this.todoRepository = todoRepository;
    }

    public String add(String input, String userId)
            throws IOException {

        AddTodo addTodo =
                mapper.readValue(input, AddTodo.class);

        Todo todo = Todo.builder()
                .userId(userId)
                .todoId(UUID.randomUUID().toString())
                .title(addTodo.getTitle())
                .description(addTodo.getDescription())
                .dateCreated(new Date())
                .complete(false)
                .build();

        todoRepository.save(todo);

        return mapper.writeValueAsString(todo);
    }

    public String allByUser(String userId)
            throws JsonProcessingException {

        List<Todo> todos = todoRepository.list(userId);

        return mapper.writeValueAsString(todos);
    }
}

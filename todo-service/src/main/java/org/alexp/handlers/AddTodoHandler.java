package org.alexp.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.alexp.repository.TodoRepository;
import org.alexp.service.TodoService;

import java.io.IOException;

public class AddTodoHandler {

    private final TodoRepository todoRepository;
    private final TodoService todoService;

    public AddTodoHandler() {

        this.todoRepository = new TodoRepository();
        this.todoService = new TodoService(todoRepository);
    }

    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input,
            Context context) {

        try {
            String body = todoService.add(input.getBody(),
                    input.getHeaders().get("email"));

            return new APIGatewayProxyResponseEvent()
                    .withBody(body)
                    .withStatusCode(200);

        } catch (IOException e) {

            return new APIGatewayProxyResponseEvent()
                    .withBody("{\"error\": \"invalid json\"}")
                    .withStatusCode(400);
        }
    }
}

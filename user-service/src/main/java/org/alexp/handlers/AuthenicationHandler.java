package org.alexp.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.alexp.exceptions.Forbidden;
import org.alexp.repository.UsersRepository;
import org.alexp.service.AuthService;

import java.io.IOException;

public class AuthenicationHandler {

    private final AuthService authService;
    private final UsersRepository userRepository;

    public AuthenicationHandler() {

        this.userRepository = new UsersRepository();
        this.authService = new AuthService(userRepository);
    }

    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input,
            Context context) {

        try {
            String body = authService.authenticate(input.getBody());

            return new APIGatewayProxyResponseEvent()
                    .withBody(body)
                    .withStatusCode(200);
        } catch (Forbidden forbidden) {

            return new APIGatewayProxyResponseEvent()
                    .withBody("{\"error\": \"forbidden\"}")
                    .withStatusCode(403);
        } catch (IOException e) {

            return new APIGatewayProxyResponseEvent()
                    .withBody("{\"error\": \"invalid json\"}")
                    .withStatusCode(400);
        }
    }
}

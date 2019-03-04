package org.alexp.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexp.exceptions.AlreadyExists;
import org.alexp.model.User;
import org.alexp.repository.UsersRepository;
import org.alexp.service.AuthService;
import org.alexp.service.RegistrationService;

import java.io.IOException;

public class RegistrationHandler {

    private final AuthService authService;
    private final UsersRepository userRepository;
    private final ObjectMapper mapper;
    private final RegistrationService registrationService;

    public RegistrationHandler() {

        authService = new AuthService();
        userRepository = new UsersRepository();
        mapper = new ObjectMapper();
        registrationService = new RegistrationService(
                authService,
                userRepository,
                mapper);
    }

    /**
     * Handles a user registration request
     *
     * @param input   Input message from Api Gateway
     * @param context Context from Api Gateway
     * @return {@link User} object saved to repository
     * @throws IOException Thrown if there is a problem
     *                     serializing/deserialing json.
     */
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input,
            Context context) {

        try {
            String body = registrationService.registrationRequest(input.getBody());

            return new APIGatewayProxyResponseEvent()
                    .withBody(body)
                    .withStatusCode(200);
        } catch (IOException e) {

            return new APIGatewayProxyResponseEvent()
                    .withBody("{\"error\": \"invalid json\"}")
                    .withStatusCode(400);
        } catch (AlreadyExists alreadyExists) {

            return new APIGatewayProxyResponseEvent()
                    .withBody("{\"error\": \"user already exists\"}")
                    .withStatusCode(400);
        }
    }
}

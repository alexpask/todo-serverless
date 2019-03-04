package org.alexp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexp.exceptions.AlreadyExists;
import org.alexp.model.User;
import org.alexp.repository.UsersRepository;

import java.io.IOException;

public class RegistrationService {

    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final ObjectMapper mapper;

    public RegistrationService(
            AuthService authService,
            UsersRepository usersRepository,
            ObjectMapper mapper) {

        this.authService = authService;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
    }

    public String registrationRequest(String input)
            throws IOException, AlreadyExists {

        User newUser = mapper.readValue(input, User.class);

        if (alreadyExists(newUser)) {

            throw new AlreadyExists();
        }

        usersRepository.save(newUser);

        newUser.setToken(
                authService.issueJwt(newUser));

        return mapper.writeValueAsString(newUser);
    }

    private boolean alreadyExists(User user) {

        if (usersRepository.get(user.getEmail()) == null) {

            return false;
        }

        return true;
    }
}

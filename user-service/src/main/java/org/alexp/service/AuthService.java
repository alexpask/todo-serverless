package org.alexp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexp.exceptions.Forbidden;
import org.alexp.model.AuthRequest;
import org.alexp.model.User;
import org.alexp.repository.UsersRepository;

import java.io.IOException;

public class AuthService {

    private final ObjectMapper mapper;
    private final Algorithm algorithm = Algorithm.HMAC256("secret");
    private UsersRepository usersRepository;

    public AuthService(UsersRepository usersRepository) {

        this();
        this.usersRepository = usersRepository;
    }

    public AuthService() {

        this.mapper = new ObjectMapper();
    }

    public String issueJwt(User user) {

        return JWT.create()
                .withClaim("user", user.getEmail())
                .sign(algorithm);
    }

    public String authenticate(String input)
            throws Forbidden, IOException {

        AuthRequest authRequest =
                mapper.readValue(input, AuthRequest.class);

        User user = usersRepository.get(authRequest.getEmail());

        if (isNull(user) || passwordIncorrect(user, authRequest)) {

            throw new Forbidden();
        }

        user.setToken(issueJwt(user));
        user.setHashedPassword(null);

        return mapper.writeValueAsString(user);
    }

    private boolean passwordIncorrect(User user, AuthRequest authRequest) {

        if (user.getHashedPassword() == null) {

            return true;
        }

        return !user.getHashedPassword().equals(authRequest.getPassword());
    }

    private boolean isNull(User user) {

        return user == null;
    }
}

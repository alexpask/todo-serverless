package org.alexp.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput
import org.alexp.model.User
import org.alexp.repository.UsersRepository
import spock.lang.Specification

class RegistrationServiceSpec extends Specification {

    def mapper = new JsonOutput()

    def authService = Mock(AuthService)
    def userRepository = Mock(UsersRepository)

    RegistrationService registrationService

    void setup() {
        registrationService =
                new RegistrationService(
                        authService,
                        userRepository,
                        new ObjectMapper())
    }

    def "should register a new user"() {

        given: "a new user request"

        def user = new User(email: "test@gmail.com", hashedPassword: "password1")
        def userJson = mapper.toJson(user)

        when: "the new user is registered"

        def response = registrationService.registrationRequest(userJson)

        then: "user is saved to repository"

        1 * userRepository.save(_ as User)

        and: "jwt is issued"

        1 * authService.issueJwt(_ as User) >> "jwt"

        and: "correct response is returned"

        response != null
    }
}

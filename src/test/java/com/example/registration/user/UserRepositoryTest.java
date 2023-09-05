package com.example.registration.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@DirtiesContext
class UserRepositoryTest {

    @Autowired
    public UserRepository testRepository;

    @BeforeEach
    void savingUsers() {
        User userToTest = new User(
                "fakemail@mail.com",
                "password",
                "John",
                "Doe",
                LocalDateTime.of(2020, 12, 12, 12, 12),
                "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&quality=85&auto=format&fit=max&s=a52bbe202f57ac0f5ff7f47166906403",
                Role.USER,
                "fakeNickName",
                "+123456789101112",
                Gender.MALE);

        testRepository.save(userToTest);
    }

    @AfterEach
    void removingData() {
        testRepository.deleteAll();
    }

    @Test
    void itShouldFindUserByEmail() {

        String email = "fakemail@mail.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";
        LocalDateTime time = LocalDateTime.of(2020, 12, 12, 12, 12);
        String avatarUrl = "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&quality=85&auto=format&fit=max&s=a52bbe202f57ac0f5ff7f47166906403";
        Role role = Role.USER;
        String nickname = "fakeNickName";
        String phoneNumber = "+123456789101112";
        Gender gender = Gender.MALE;

        Optional<User> userToMatch = testRepository.findByEmail(email);
        assertTrue(userToMatch.isPresent());
        assertEquals(password, userToMatch.get().getPassword());
        assertEquals(firstName, userToMatch.get().getFirstName());
        assertEquals(lastName, userToMatch.get().getLastName());
        assertEquals(time, userToMatch.get().getCreated());
        assertEquals(avatarUrl, userToMatch.get().getAvatarUrl());
        assertEquals(role, userToMatch.get().getRole());
        assertEquals(nickname, userToMatch.get().getNickname());
        assertEquals(phoneNumber, userToMatch.get().getPhoneNumber());
        assertEquals(gender, userToMatch.get().getGender());
    }
    @Test
    void itShouldDeleteUserByEmail() {
        String email = "fakemail@mail.com";
        testRepository.deleteUserByEmail(email);

        Optional<User> emptyUser = testRepository.findByEmail(email);
        if (emptyUser.isEmpty()) {
            assertEquals(emptyUser, Optional.empty());
        }
    }
}
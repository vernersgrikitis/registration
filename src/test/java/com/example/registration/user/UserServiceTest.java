package com.example.registration.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private UserService testService;


    @BeforeEach
    void setUp() {
        testService = new UserService(userRepository, eventPublisher);
    }

    @Test
    void getAllUsers() {
        testService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    @Disabled
    void save() {
        User userToSave = new User(
                "fakemail@mail.com",
                "password",
                "John",
                "Doe",
                LocalDateTime.of(2012,12, 12, 12, 12),
                "https://t1.gstatic.com/licensed-image?q=tbn:ANd9GcRRv9ICxXjK-LVFv-lKRId6gB45BFoNCLsZ4dk7bZpYGblPLPG-9aYss0Z0wt2PmWDb",
                Role.USER,
                "fakeNickName",
                "+123456789010121314",
                Gender.MALE);

        testService.save(userToSave);

    }

    @Test
    @Disabled
    void deleteUser() {
    }

    @Test
    @Disabled
    void findUserByEmail() {
    }

    @Test
    @Disabled
    void updateUserAvatar() {
    }
}
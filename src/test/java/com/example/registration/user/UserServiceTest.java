//package com.example.registration.user;
//
//import com.example.registration.events.CustomUpdateEvent;
//import com.example.registration.events.UserDeletedEvent;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.web.server.ResponseStatusException;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@RequiredArgsConstructor
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//    private UserService testService;
//
//
//    @BeforeEach
//    void setUp() {
//        testService = new UserService(userRepository, eventPublisher);
//    }
//
//    @Test
//    void getAllUsers() {
//        testService.getAllUsers();
//        verify(userRepository).findAll();
//    }
//
//    @Test
//    void save() {
//        User userToSave = new User(
//                "samemail@mail.com",
//                "password",
//                "John",
//                "Doe",
//                LocalDateTime.of(2012,12, 12, 12, 12),
//                "https://t1.gstatic.com/licensed-image?q=tbn:ANd9GcRRv9ICxXjK-LVFv-lKRId6gB45BFoNCLsZ4dk7bZpYGblPLPG-9aYss0Z0wt2PmWDb",
//                Role.USER,
//                "fakeNickName",
//                "+123456789010121314",
//                Gender.MALE);
//
//        testService.save(userToSave);
//
//        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
//
//        verify(userRepository).save(userArgumentCaptor.capture());
//
//        User capturedUser = userArgumentCaptor.getValue();
//
//        assertThat(capturedUser.getEmail()).isEqualTo(userToSave.getEmail());
//        assertThat(capturedUser.getPassword()).isEqualTo(userToSave.getPassword());
//        assertThat(capturedUser.getFirstName()).isEqualTo(userToSave.getFirstName());
//        assertThat(capturedUser.getLastName()).isEqualTo(userToSave.getLastName());
//        assertThat(capturedUser.getCreated()).isEqualTo(userToSave.getCreated());
//        assertThat(capturedUser.getImage()).isEqualTo(userToSave.getImage());
//        assertThat(capturedUser.getRole()).isEqualTo(userToSave.getRole());
//        assertThat(capturedUser.getNickname()).isEqualTo(userToSave.getNickname());
//        assertThat(capturedUser.getPhoneNumber()).isEqualTo(userToSave.getPhoneNumber());
//        assertThat(capturedUser.getGender()).isEqualTo(userToSave.getGender());
//    }
//
//    @Test
//    void willThrowExceptionIfEmailIsTaken() {
//
//        User userWithSameEmail = new User();
//
//        userWithSameEmail.setEmail("samemail@mail.com");
//        userWithSameEmail.setPassword("password");
//        userWithSameEmail.setFirstName("Jane");
//        userWithSameEmail.setLastName("Doe");
//
//        testService.save(userWithSameEmail);
//
//        given(userRepository.existsUsersByEmail(userWithSameEmail.getEmail()))
//                .willReturn(true);
//
//        assertThatThrownBy(() -> testService.save(userWithSameEmail))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining("User with " + userWithSameEmail.getEmail() + " is already registered");
//
//    }
//
//    @Test
//    void shouldDeleteUser() {
//
//        String email = "user@example.com";
//
//        User user = new User();
//        user.setEmail(email);
//
//        testService.deleteUser(user);
//
//        verify(userRepository, times(1)).deleteUserByEmail(email);
//        verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class));
//    }
//
//    @Test
//    void shouldFindUserByEmail() {
//
//        User user = new User(
//                "samemail@mail.com",
//                "password",
//                "John",
//                "Doe",
//                LocalDateTime.of(2012,12, 12, 12, 12),
//                "https://t1.gstatic.com/licensed-image?q=tbn:ANd9GcRRv9ICxXjK-LVFv-lKRId6gB45BFoNCLsZ4dk7bZpYGblPLPG-9aYss0Z0wt2PmWDb",
//                Role.USER,
//                "fakeNickName",
//                "+123456789010121314",
//                Gender.MALE);
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        User foundUser = testService.findUserByEmail(user.getEmail());
//
//
//        assertNotNull(foundUser);
//        assertEquals(user.getEmail(), foundUser.getUsername());
//    }
//
//    @Test
//    void shouldNotFoundUserNonExistingEmail() {
//
//        String email = "notexisting@example.com";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        assertThrows(ResponseStatusException.class, () -> testService.findUserByEmail(email));
//    }
//
//    @Test
//
//    void updateUserAvatar() {
//
//        User user = new User(
//                "samemail@mail.com",
//                "password",
//                "John",
//                "Doe",
//                LocalDateTime.of(2012,12, 12, 12, 12),
//                "https://t1.gstatic.com/licensed-image?q=tbn:ANd9GcRRv9ICxXjK-LVFv-lKRId6gB45BFoNCLsZ4dk7bZpYGblPLPG-9aYss0Z0wt2PmWDb",
//                Role.USER,
//                "fakeNickName",
//                "+123456789010121314",
//                Gender.MALE);
//
//        String avatarUrl = "https://mymodernmet.com/wp/wp-content/uploads/archive/5LJIaEyCgXUHj3RMK6qp_robertsijka1.jpg";
//
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        testService.updateUserAvatar(user, avatarUrl);
//
//        assertEquals(avatarUrl, user.getImage());
//        verify(userRepository, times(1)).save(user);
//        verify(eventPublisher, times(1)).publishEvent(any(CustomUpdateEvent.class));
//
//    }
//}
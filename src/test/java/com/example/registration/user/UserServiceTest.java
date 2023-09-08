package com.example.registration.user;

import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private UserServiceImpl testService;


    @BeforeEach
    void setUp() {
        testService = new UserServiceImpl(userRepository, eventPublisher);
    }

    @Test
    void shouldSave() throws IOException {

        var imageFile = new ClassPathResource("image/catimage.jpg");
        byte[] bytes = StreamUtils.copyToByteArray(imageFile.getInputStream());

        Image image = new Image();
        image.setName("catimage.jpg");
        image.setType("image/jpeg");
        image.setImage(bytes);

        User userToSave = new User(
                "samemail@mail.com",
                "password",
                "John",
                "Doe",
                LocalDateTime.of(2012,12, 12, 12, 12),
                image,
                Role.USER,
                "fakeNickName",
                "+123456789010121314",
                Gender.MALE);

        testService.save(userToSave);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(userToSave.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(userToSave.getPassword());
        assertThat(capturedUser.getFirstName()).isEqualTo(userToSave.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(userToSave.getLastName());
        assertThat(capturedUser.getCreated()).isEqualTo(userToSave.getCreated());
        assertThat(capturedUser.getImage()).isEqualTo(userToSave.getImage());
        assertThat(capturedUser.getRole()).isEqualTo(userToSave.getRole());
        assertThat(capturedUser.getNickname()).isEqualTo(userToSave.getNickname());
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(userToSave.getPhoneNumber());
        assertThat(capturedUser.getGender()).isEqualTo(userToSave.getGender());
    }

    @Test
    public void testValidEmail() {
        User user = new User();
        user.setEmail("valid@example.com");

        when(userRepository.existsUsersByEmail(user.getEmail())).thenReturn(false);

        testService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");

        try {
            testService.save(user);
        } catch (ResponseStatusException ex) {
            assert ex.getStatusCode() == HttpStatus.BAD_REQUEST;
            assert ex.getReason().contains("Invalid email format");
        }
    }

    @Test
    void willThrowExceptionIfEmailIsTaken() {

        User userWithSameEmail = new User();

        userWithSameEmail.setEmail("samemail@mail.com");
        userWithSameEmail.setPassword("password");
        userWithSameEmail.setFirstName("Jane");
        userWithSameEmail.setLastName("Doe");

        testService.save(userWithSameEmail);

        given(userRepository.existsUsersByEmail(userWithSameEmail.getEmail()))
                .willReturn(true);

        assertThatThrownBy(() -> testService.save(userWithSameEmail))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User with " + userWithSameEmail.getEmail() + " is already registered");

    }

    @Test
    void shouldDeleteUser() {

        String email = "user@user.com";

        User user = new User();
        user.setEmail(email);

        testService.deleteUser(user);

        verify(userRepository, times(1)).deleteUserByEmail(email);
        verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class));
    }

    @Test
    void shouldFindUserByEmail() {

        User user = new User();
        user.setRole(Role.USER);
        user.setEmail("samemail@mail.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = testService.findUserByEmail(user.getEmail());


        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getUsername());
    }

    @Test
    void shouldNotFoundUserNonExistingEmail() {

        String email = "notexisting@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> testService.findUserByEmail(email));
    }

    @Test
    public void shouldUpdateUserImage() throws IOException {

        String username = "user@user.com";

        var imageFile = new ClassPathResource("image/catimage.jpg");
        String content = "catimage";
        byte[] bytes = StreamUtils.copyToByteArray(imageFile.getInputStream());

        MockMultipartFile file = new MockMultipartFile(content, bytes);

        Optional<User> optionalUser = Optional.of(new User());

        when(userRepository.findByEmail(username)).thenReturn(optionalUser);

        testService.updateUserImage(username, file);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldNotGetImageByEmailWhenImageDoesNotExist() {

        String email = "none@none.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class, () -> testService.getImageByEmail(email));

    }

}
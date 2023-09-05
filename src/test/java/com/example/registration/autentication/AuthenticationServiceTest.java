package com.example.registration.autentication;

import com.example.registration.configuration.JwtService;
import com.example.registration.user.User;
import com.example.registration.user.UserRepository;
import com.example.registration.user.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    private AuthenticationService testAuthenticationService;

    @BeforeEach
    void setUp() {
        testAuthenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void testRegister() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("fakeMail@fakemail.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        AuthenticationResponse response = testAuthenticationService.register(request);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    void registrationResponse() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("fake@fakemail.com");
        request.setPassword("password");
        request.setFirstName("Jane");
        request.setLastName("Doe");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(ResponseStatusException.class, () -> testAuthenticationService.register(request));
    }

    @Test
    void authenticate() {

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("fakeMail@fakemail.com");
        request.setPassword("password");

        User user = User.builder()
                .email("user@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("jwtToken");

        AuthenticationResponse response = testAuthenticationService.authenticate(request);

        assertNotNull(response);
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtService, times(1)).generateToken(Mockito.any(UserDetails.class));
    }
}
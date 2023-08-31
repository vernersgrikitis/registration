package com.example.registration.autentication;

import com.example.registration.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.registration.user.Role;
import com.example.registration.user.User;
import com.example.registration.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with " + request.getEmail() + " already exist! ");
        }
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .created(LocalDateTime.now())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return registrationResponse(user);
    }

    public AuthenticationResponse registrationResponse(User user) {
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var jwtToken = jwtService.generateToken(user);
        new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

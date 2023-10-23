package com.example.registration.autentication;

import com.example.registration.configuration.JwtService;
import com.example.registration.user.Role;
import com.example.registration.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with " + request.getEmail() + " already exist! ");
        }
        User user = new User();
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setRole(Role.USER);
        userService.save(user);

        return response(user);
    }

    public AuthenticationResponse response(User user) {
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String userPassword = user.getPassword();

        if (!passwordEncoder.matches(request.getPassword(), userPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password you entered is wrong");
        }
        return response(user);
    }
}

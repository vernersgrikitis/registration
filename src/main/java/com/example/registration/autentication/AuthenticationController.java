package com.example.registration.autentication;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public void register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
    }

    @PostMapping("/logging-in")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }



}

package com.example.registration.autentication;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @CrossOrigin
    @PostMapping("/registration")
    public void register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logging-in")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }



}

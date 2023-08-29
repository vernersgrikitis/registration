package com.example.registration.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/logging-in/user")
    @PreAuthorize("hasAuthority(Role.USER)")
    public String hello() {
        return "Welcome to User Profile! ";
    }
    @PostMapping("/logging-in/user/add-avatar")
    @PreAuthorize("hasAuthority(Role.USER)")
    public String addAvatarToUser(@RequestBody String avatarUrl) {

        return avatarUrl;
    }

}

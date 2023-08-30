package com.example.registration.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Data
@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteUser(UserDetails userDetails) {
        final String email = userDetails.getUsername();
        userRepository.deleteUserByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with " + email + " not found!"));
    }


    public void updateUserAvatar(UserDetails userDetails, String avatarUrl) {
        final String email = userDetails.getUsername();
        User currentUser = findUserByEmail(email);
        currentUser.setAvatarUrl(avatarUrl);
        save(currentUser);
    }


}

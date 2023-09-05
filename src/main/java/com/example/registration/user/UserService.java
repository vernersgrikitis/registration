package com.example.registration.user;

import com.example.registration.events.CustomUpdateEvent;
import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with " + user.getEmail() + " is already registered");
        }
        checkUserEmail(user.getEmail());
        checkUserValues(user.getPassword());
        checkUserValues(user.getFirstName());
        checkUserValues(user.getLastName());
        userRepository.save(user);
    }

    public boolean checkUserValues(String value) {
        return value == null || value.isBlank() || value.isEmpty();
    }

    public boolean checkUserEmail(String value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches() || value == null || value.isBlank() || value.isEmpty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UserDetails userDetails) {
        final String email = userDetails.getUsername();
        userRepository.deleteUserByEmail(email);
        eventPublisher.publishEvent(new UserDeletedEvent(this, userDetails));

    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with " + email + " not found!"));
    }

    public void updateUserAvatar(UserDetails userDetails, String avatarUrl) {
        final String email = userDetails.getUsername();
        User currentUser = findUserByEmail(email);
        currentUser.setAvatarUrl(avatarUrl);
        save(currentUser);
        eventPublisher.publishEvent(new CustomUpdateEvent(this, userDetails.getUsername(), avatarUrl));
    }


}

package com.example.registration.user;

import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void save(User user) {
        if (!checkUserEmail(user.getEmail())){
            String errorMessage = "Invalid email format: " + user.getEmail();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        boolean existEmail = userRepository.existsUsersByEmail(user.getEmail());

        if (existEmail) {
            String errorMessage = "User with " + user.getEmail() + " is already registered";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,errorMessage);
        }
        userRepository.save(user);
    }

    private boolean checkUserEmail(String value) {
        String regex = "(\\S.*\\S)(@)(\\S.*\\S)(.\\S[a-z]{2,3})";
        return value.matches(regex);
    }

    @Override
    public void deleteUser(UserDetails userDetails) {
        final String email = userDetails.getUsername();
        userRepository.deleteUserByEmail(email);
        eventPublisher.publishEvent(new UserDeletedEvent(this, userDetails));

    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with " + email + " not found!"));
    }

    @Override
    public void updateUserImage(String username, MultipartFile file) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User currentUser = new User();
        if (optionalUser.isPresent()) {
            currentUser = optionalUser.get();
        }
        currentUser.setImage(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build());
        userRepository.save(currentUser);

    }

    @Override
    public ResponseEntity<byte[]> getImageByEmail(String email) throws DataFormatException, IOException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        if (user.getImage().getImage() != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(user.getImage().getType()))
                    .body(ImageUtility.decompressImage(user.getImage().getImage()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found! ");
    }
}

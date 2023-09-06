package com.example.registration.user;

import com.example.registration.events.CustomUpdateEvent;
import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void save(User user) {
        boolean existEmail = userRepository.existsUsersByEmail(user.getEmail());

        if (existEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with " + user.getEmail() + " is already registered");
        }
        userRepository.save(user);
    }

    public boolean checkUserEmail(String value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public void deleteUser(UserDetails userDetails) {
        final String email = userDetails.getUsername();
        userRepository.deleteUserByEmail(email);
        eventPublisher.publishEvent(new UserDeletedEvent(this, userDetails));

    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with " + email + " not found!"));
    }

    public void updateUserAvatar(UserDetails userDetails, MultipartFile file) throws IOException {
        String email = userDetails.getUsername();
        User currentUser = findUserByEmail(email);
        currentUser.setImage(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build());
        save(currentUser);
//        eventPublisher.publishEvent(new CustomUpdateEvent(this, userDetails.getUsername(), file.getName()));
    }

    public byte[] getImage(String username) throws DataFormatException, IOException {
        User user = findUserByEmail(username);
        Image image = new Image();

        image.setImage(user.getImage().getImage());
        image.setName(user.getImage().getName());
        image.setType(user.getImage().getType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(image.getType()));

        return ImageUtility.decompressImage(image.getImage());
    }
}

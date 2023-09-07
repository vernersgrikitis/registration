package com.example.registration.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface UserService {

    void save(User user);

    void updateUserImage(String username, MultipartFile file) throws IOException;

    ResponseEntity<byte[]> getImageByEmail(String email) throws DataFormatException, IOException;

    void deleteUser(UserDetails userDetails);

    User findUserByEmail(String email);
}

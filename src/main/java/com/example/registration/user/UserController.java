package com.example.registration.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.zip.DataFormatException;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/secure-endpoint", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findUserByEmail(email);
        return userDetails.getUsername() + " You are in secure endpoint! " + user.getFirstName() + " " + user.getLastName();
    }

    @RequestMapping(value = "/add-image", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String updateUserImage(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam("image") MultipartFile file) throws IOException {
        userService.updateUserImage(userDetails.getUsername(), file);
        return "Image uploaded successfully: " + file.getOriginalFilename();
    }

    @RequestMapping(value = "/get-image", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public ResponseEntity<byte[]> previewUserImage(@AuthenticationPrincipal UserDetails userDetails) throws DataFormatException, IOException {
        String username = userDetails.getUsername();
        return userService.getImageByEmail(username);
    }

    @RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails);
        return "User deleted successfully! ";
    }
}

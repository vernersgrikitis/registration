package com.example.registration.user;

import lombok.RequiredArgsConstructor;
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
        return userDetails.getUsername() + " You are in secure endpoint! ";
    }

    @RequestMapping(value = "/add-image", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String updateUserAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("image") MultipartFile file) throws IOException {
        userService.updateUserAvatar(userDetails, file);
        return "Avatar updated successfully! ";
    }

    @RequestMapping(value = "/get-image", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public byte[] previewUserAvatar(@AuthenticationPrincipal UserDetails userDetails) throws DataFormatException, IOException {
        String username = userDetails.getUsername();
        return userService.getImage(username);
    }

    @RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails);
        return "User deleted successfully! ";
    }
}

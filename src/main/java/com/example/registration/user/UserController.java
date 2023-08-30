package com.example.registration.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/logging-in")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }

    @RequestMapping(value = "/user/add-avatar", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String updateUserAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody String avatarUrl) {
        userService.updateUserAvatar(userDetails, avatarUrl);
        return avatarUrl;
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails);
        return new ResponseEntity<>("User deleted successfully! ", HttpStatus.OK);
    }
}

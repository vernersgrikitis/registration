package com.example.registration.user;

import com.example.registration.events.CustomUpdateEvent;
import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/logging-in")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }

    @RequestMapping(value = "/user/add-avatar", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public ResponseEntity<String> updateUserAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody String avatarUrl) {
        userService.updateUserAvatar(userDetails, avatarUrl);
        eventPublisher.publishEvent(new CustomUpdateEvent(this, userDetails.getUsername(), avatarUrl));

        return new ResponseEntity<>("Avatar updated successfully with URL " + avatarUrl, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority(Role.USER)")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails);
        eventPublisher.publishEvent(new UserDeletedEvent(this, userDetails));
        return new ResponseEntity<>("User deleted successfully! ", HttpStatus.OK);
    }
}

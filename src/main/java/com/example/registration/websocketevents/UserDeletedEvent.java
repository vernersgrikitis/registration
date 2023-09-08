package com.example.registration.websocketevents;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.userdetails.UserDetails;
@Getter
public class UserDeletedEvent extends ApplicationEvent {

    private UserDetails userDetails;
    public UserDeletedEvent(Object source, UserDetails userDetails) {
        super(source);
        this.userDetails = userDetails;
    }
}

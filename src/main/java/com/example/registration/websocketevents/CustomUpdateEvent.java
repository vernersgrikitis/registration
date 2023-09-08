package com.example.registration.websocketevents;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CustomUpdateEvent extends ApplicationEvent {

    private String username;
    private String event;

    public CustomUpdateEvent(Object source, String username, String event) {
        super(source);
        this.username = username;
        this.event = event;
    }

}

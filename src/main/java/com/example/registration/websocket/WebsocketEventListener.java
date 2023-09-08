package com.example.registration.websocket;

import com.example.registration.websocketevents.CustomUpdateEvent;
import com.example.registration.websocketevents.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleAvatarUpdate(CustomUpdateEvent event) {
        String text = event.getEvent();
        messagingTemplate.convertAndSend(
                "/secured/avatar/" + event.getUsername(),
                "Image updated successfully! " + text);
    }

    @EventListener
    public void handleUserDeletedEvent(UserDeletedEvent event) {

        messagingTemplate.convertAndSend(
                "/secured/avatar/" + event.getUserDetails().getUsername(),
                "User deleted successfully! ");

    }
}

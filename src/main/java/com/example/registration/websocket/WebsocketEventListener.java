package com.example.registration.websocket;

import com.example.registration.events.CustomUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleAvatarUpdate(CustomUpdateEvent event) {
        String avatarUrl = event.getEvent();
        messagingTemplate.convertAndSend("/secured/avatar/" + event.getUsername(), "Avatar updated successfully with URL " + avatarUrl);
    }

//    @EventListener
//    public void handleUserDeletedEvent(UserDeletedEvent event) {
//        UserDetails userDetails = event.getUserDetails();
//        String username = userDetails.getUsername();
//
//        messagingTemplate.convertAndSendToUser();
//        userDetails.closeUserSocket(username);
//    }
}

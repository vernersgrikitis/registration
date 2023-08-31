package com.example.registration.websocket;

import com.example.registration.events.CustomUpdateEvent;
import com.example.registration.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final SocketManagerService socketManagerService;

    @EventListener
    public void handleAvatarUpdate(CustomUpdateEvent event) {
        String user = event.getUsername();
        String avatarUrl = event.getEvent();
        messagingTemplate.convertAndSendToUser(user, "/topic/avatar-update",
                "Avatar updated successfully with URL " + avatarUrl);
    }

    @EventListener
    public void handleUserDeletedEvent(UserDeletedEvent event) throws IOException {
        UserDetails userDetails = event.getUserDetails();
        socketManagerService.closeUserSocket(userDetails.getUsername());
    }
}

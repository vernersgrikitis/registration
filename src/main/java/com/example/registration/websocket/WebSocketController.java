package com.example.registration.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketMessage;

@Controller
public class WebSocketController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public WebSocketMessage handleMessage(WebSocketMessage message) {
        return message;
    }

}

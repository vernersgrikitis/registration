package com.example.registration.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/message")
    @SendTo("/topic/public")
    public  Message sendMessage(@Payload Message message){
        return message;
    }

}

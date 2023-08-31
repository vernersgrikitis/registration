package com.example.registration.websocket;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    private String content;
    private String userToSend;
}

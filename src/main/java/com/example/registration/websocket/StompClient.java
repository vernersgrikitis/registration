package com.example.registration.websocket;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class StompClient {

    private static String URL = "ws://localhost:8080/secured/room";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new StringMessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZXJuZXJzc0BnbWFpbC5jb20iLCJpYXQiOjE2OTM4NTQ5ODcsImV4cCI6MTY5Mzg1Njc4N30.6DirfYIz8c7dOA7KcO7nLrsuDsOM9emq4uiOD70l8FU");
        var future = stompClient.connectAsync(URL, handshakeHeaders, sessionHandler);
        future.get();

        new Scanner(System.in).nextLine();
    }

}
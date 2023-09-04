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
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZXJuZXJzc0BnbWFpbC5jb20iLCJpYXQiOjE2OTM4NTE5MTksImV4cCI6MTY5Mzg1MzcxOX0.qh5R750TS4PLI2SX2HHde3MOLEvgqwcldsCfd9bgHJ4");
        var future = stompClient.connectAsync(URL, handshakeHeaders, sessionHandler);
        future.get();

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }

}

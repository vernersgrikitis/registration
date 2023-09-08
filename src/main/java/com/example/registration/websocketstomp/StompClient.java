package com.example.registration.websocketstomp;

import com.example.registration.autentication.AuthenticationRequest;
import com.example.registration.autentication.AuthenticationResponse;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class StompClient {

    private static String URL = "ws://localhost:8080/secured/room";

    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new StringMessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();

        Scanner scan = new Scanner(System.in);

        String email;
        String password;

        System.out.println("Enter your email! ");
        email = scan.nextLine();

        System.out.println("Enter your password! ");
        password = scan.nextLine();

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        AuthenticationResponse response = restTemplate.postForObject("http://localhost:8080/login", request, AuthenticationResponse.class);

        System.out.println(response.getToken());

        handshakeHeaders.add("Authorization",
                "Bearer " + response.getToken());
        var future = stompClient.connectAsync(URL, handshakeHeaders, sessionHandler);
        future.get();

//        new Scanner(System.in).nextLine();
    }

}

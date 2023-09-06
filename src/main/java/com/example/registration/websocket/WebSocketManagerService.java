package com.example.registration.websocket;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketManagerService {

    private Map<String, Socket> userSocketMap = new ConcurrentHashMap<>();

    public void closeUserSocket(String username) throws IOException {
        Socket socket = userSocketMap.get(username);
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        userSocketMap.remove(username);
    }

}

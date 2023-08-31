package com.example.registration.websocket;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketManagerService {

    private Map<String, Socket> userSocketMap = new ConcurrentHashMap<>();

    public void addUserSocket(String username, Socket socket) {
        userSocketMap.put(username, socket);
    }

    public Socket getUserSocket(String username) {
        return userSocketMap.get(username);
    }

    public void closeUserSocket(String username) throws IOException {
        Socket socket = userSocketMap.get(username);
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        userSocketMap.remove(username);
    }

    public boolean hasUserSocket(String username) {
        return userSocketMap.containsKey(username);
    }
}

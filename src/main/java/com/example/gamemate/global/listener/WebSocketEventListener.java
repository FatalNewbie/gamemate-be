package com.example.gamemate.global.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final Map<String, String> sessionLoginMap = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String login = headerAccessor.getFirstNativeHeader("login");
        String roomId = headerAccessor.getFirstNativeHeader("roomId");

        if (sessionId != null) {
            if (login != null) {
                sessionLoginMap.put(sessionId, login);
            }
            if (roomId != null) {
                sessionRoomMap.put(sessionId, roomId);
            }
            logger.info("Received a new web socket connection, sessionId: {}, login: {}, roomId: {}", sessionId, login, roomId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        if (sessionId != null) {
            String login = sessionLoginMap.remove(sessionId);
            String roomId = sessionRoomMap.remove(sessionId);
            logger.info("Web socket connection closed, sessionId: {}, login: {}, roomId: {}", sessionId, login, roomId);
        }
    }
}

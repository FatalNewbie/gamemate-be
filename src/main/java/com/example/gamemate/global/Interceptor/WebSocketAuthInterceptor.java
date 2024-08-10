package com.example.gamemate.global.Interceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
/*preSend 메소드는 메시지가 실제 채널로 전송되기 직전에 호출됨
* 클라이언트가 메시지를 보낼떄 : 메시지가 실제로 서버에 도착하기 전에 preSend호출
* 서버가 메시지를 보낼 때 : 클라이언트로의 메시지가 채널을 통해 전송되기 직전에 preSend호출
* 여기서는 웹소켓 메시지에 사용자의 인증정보를 추가하는 용도로 사용.*/


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // SecurityContextHolder는 애플리케이션의 현재 실행 스레드와 연관된 SecurityContext를 보관하는 역할을 합니다.
        // 이 컨텍스트는 주로 HTTP 요청을 통해 인증된 정보가 저장되지만, 웹소켓 연결을 포함한 다른 방식으로도 인증된 정보가 저장될 수 있습니다.


        if (accessor != null && accessor.getCommand() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                accessor.setUser(auth);
            }
        }

        return message;
    }
}

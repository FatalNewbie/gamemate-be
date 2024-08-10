package com.example.gamemate.global.config;

import com.example.gamemate.domain.auth.jwt.JWTUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;

@Slf4j
@Configuration
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer{
    /*
    * WebSocketAuthConfig 클래스는 웹소켓 메시지 브로커를 설정하는 역할을 합니다.
    * 특히 configureClientInboundChannel 메서드는 클라이언트로부터 들어오는 웹소켓 메시지를 처리하는 채널에 인터셉터를 추가합니다.
    * 이 인터셉터는 메시지가 클라이언트에서 서버로 들어올 때 특정 작업을 수행할 수 있도록 합니다. preSend 메서드는 메시지가 실제로 채널을 통해 전송되기 직전에 호출됩니다.
    * 클라이언트가 웹소켓 연결을 시도할 때(STOMP CONNECT 명령),클라이언트가 웹소켓을 통해 메시지를 보낼 때(STOMP 명령)의 모든 메시지를 가로챔.
    * 클라이언트에서 메시지를 보냈을때 서버에서 가장 먼저 받는 곳이 configureClientInboundChannel이 메소드임
    * 인증, 권한 부여, 로깅, 메시지 수정 등이 가능
*  */

    @Autowired
    private JWTUtil jwtUtil;

//      SecurityContextHolder의 전략을 MODE_INHERITABLETHREADLOCAL로 설정하여 자식 스레드가 부모 스레드의 보안 컨텍스트를 상속받도록 함.
//    @PostConstruct
//    public void init() {
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // 스톰프 메시지가 CONNECT 메시지일때.
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");

                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        if (!jwtUtil.isExpired(token)) {
                            String username = jwtUtil.getUsername(token);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            accessor.setUser(authentication);
                            // webSocket 세션에 인증 정보 저장
                            accessor.getSessionAttributes().put("auth", authentication);
                            // 핸드쉐이크 키. 해쉬맵
                        } else {
                            log.error("Token is expired at WebSocketAuthConfig");
                            throw new IllegalArgumentException("Invalid token");
                        }
                    } else {
                        log.error("Token is missing or does not start with 'Bearer 'at WebSocketAuthConfig");
                        throw new IllegalArgumentException("No token provided");
                    }
                }

                //log.debug("Current Message: {}, Thread: {}, SecurityContextHolder.getContext().getAuthentication() : {}", accessor.getCommand(),Thread.currentThread().getName(), SecurityContextHolder.getContext().getAuthentication());


                log.debug("Current Message: {}, auth from session: {}", accessor.getCommand(),(Authentication) accessor.getSessionAttributes().get("auth"));

                // 스톰프 메시지가 SEND 혹은 SUBSCRIBE 일때
                if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    // WebSocket 세션에서 인증 정보 가져오기
                    Authentication auth = (Authentication) accessor.getSessionAttributes().get("auth");
                    // SecurityContextHolder 에서 인증 정보 가져오기
                    //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.isAuthenticated()) {
                        // 메시지 처리 시점에 SecurityContext 재설정
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);
                    }
                }




                return message;
            }
        });
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/ws/**", config);
        return new CorsFilter(source);
    }
}

package com.example.gamemate.global.config;


import com.example.gamemate.global.Interceptor.WebSocketAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // stomp를 사용하기위한 어노테이션
public class WebSocketStomConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:3000}") // 환경변수와 기본값 설정
    private String allowedOrigins;

    //private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // <-- 웹소켓 핸드쉐이크로 커넥션 형성. 웹소켓은 HTTP로 핸드쉐이크 한 후에 웹소켓 프로토콜로 변환. 커넥션 형성. 이 연결을 위한 엔드포인트.
                .setAllowedOrigins(allowedOrigins) // <-- /ws 엔드포인트에 대해 http://localhost:3000에서의 요청을 허용
                //.addInterceptors(new JwtHandshakeInterceptor ()) // 인터셉터 추가
                .withSockJS(); // <- 웹소켓을 지원하지않는 브라우저에서 sockjs를 통해 실시간 통신을 지원.
    }
    // ws로 연결을 시도하면 이 서버에서는 커넥션을 쉷해주고 스톰프 프레임들을 해당 커넥션으로 전송하기 시작함.

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 앱 프리픽스를 가진 모든 메시지 요청은 @MessageMapping 경로를 탄다.
        config.setApplicationDestinationPrefixes("/app");
        // @Controller객체의 @MessageMapping 메서드로 라우팅

        config.enableSimpleBroker("/topic","/queue");
        // 메시지 브로커의 목적지로 토픽과 큐를 설정.
        // 일반적으로 토픽으로 시작하는 목적지는 메시지를 채팅방에 브로드캐스팅 하는 일대다 관계를 의미
        // 큐로 시작하는 목적지는 메시지교환과 같은 일대일 관계를 의미
    }


    // WebSocket 메시지 브로커 설정에서 클라이언트로부터 들어오는 메시지를 처리하는 채널을 구성하는 역할
    // 이 메서드에서 ChannelRegistration 객체를 사용하여 인터셉터를 등록
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(webSocketAuthInterceptor);
//    }
}


/**
 * ws엔드포인트로 웹소켓을 연결해주고
 * 그 이후에 /topic/chat 등을 사용하여 메시지를 서버에게 보냄.
 */

package lee.projectdemo.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        ///stomp/chat는 WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로이다.
        registry.addEndpoint("/stomp/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setPathMatcher(new AntPathMatcher(".")); // url을 chat/room/3 -> chat.room.3으로 참조하기 위한 설정

//        /pub 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅된다.
        registry.setApplicationDestinationPrefixes("/pub");

        //외부  메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공한다.
        //또한 /queue", "/topic", "/exchange", "/amq/queue로 시작하는 "destination" 헤더를 가진 메세지를 브로커로 라우팅한다.
//        해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행


        registry.enableStompBrokerRelay("/topic", "/exchange")
                .setRelayHost(host); //docker로 인한 relayHost 재설정
//                .setRelayHost("/epic_shamir") //docker로 인한 relayHost 재설정
//                .setRelayHost("172.17.0.2") //docker로 인한 relayHost 재설정
//                .setRelayPort(61613);
    }

}

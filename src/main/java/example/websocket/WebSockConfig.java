package example.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
//@RequiredArgsConstructor
public class WebSockConfig
//        implements WebSocketConfigurer
{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        /*
            Spring에서 Bean은 싱글톤으로 관리되지만,
            @ServerEndpoint 클래스는 WebSocket이 생성될 때마다 인스턴스가 생성되고
            JWA에 의해 관리되기 때문에 Spring의 @Autowired가 설정된 멤버들이 초기화 되지 않습니다.
            연결해주고 초기화해주는 클래스가 필요합니다.
         */
        return new ServerEndpointExporter();
    }

//    private final WebSocketHandler webSocketHandler;
//
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        System.out.println("[ WebSocket config ]");
//        registry.addHandler(webSocketHandler, "/chat").setAllowedOrigins("*");
//    }
}

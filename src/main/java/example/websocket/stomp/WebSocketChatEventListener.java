package example.websocket.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketChatEventListener {


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    // 연결 시 리스너
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        log.info("Received a new web socket connection");
        System.out.println("event: " + event.toString());
    }

    // 연결 종료 시 리스너
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {

            Message message = new Message();
            //message.setChannelId("");

            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }

}

package example.websocket.stomp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    // /pub/hello 로 send 시 메시지 전달
    @MessageMapping("/hello")
    public void message(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {

        log.info("[Message received]: {}", message);
        // username 헤더에 유저정보 추가
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        // 전송
        simpMessageSendingOperations.convertAndSend("/topic/" + message.getChannelId(), message);
    }

    // postman 으로 메시지 보내는 테스트
    @RequestMapping("/send/channel/{channelId}")
    public void sendMessage(@PathVariable("channelId") String channelId, @RequestParam String message) {

        log.info("[RestController send]: channelId: {}, message: {}", channelId, message);

        simpMessageSendingOperations.convertAndSend("/topic/" + channelId, message);
    }


}

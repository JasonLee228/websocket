package example.websocket;

//import jakarta.servlet.http.HttpServletRequest; // java-17, spring 2.7.x
//import jakarta.servlet.http.HttpSession;
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
import javax.servlet.http.HttpServletRequest; // java-11, spring 3.0
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @ServerEndpoint, @Service > WebSocketChatting 클래스를 서비스(WebSocket)로 등록합니다.
 */
@ServerEndpoint(value = "/websocket", configurator = WebSocketSessionConfigurator.class)// "/websocket" / "/"
@Service
public class WebSocketHandler {
    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    // @OnOpen > 사용자가 접속하면 session을 추가합니다.
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {

        // 세션 정보를 어떻게 얻지,,,
        /*
         *
         * 주기적으로 스케줄 돌려서 연결 정상인지 확인하는건 필요하겠다
        * */
//        Map<String, Object> userProperties = config.getUserProperties();
//        System.out.println("userProperties = " + userProperties);
//
//        // Get the HttpServletRequest object
//        HttpServletRequest request = (HttpServletRequest) config.getUserProperties().get(HttpSession.class.getName());
//
//        String ip = getClientAccessIp(request);
//        System.out.println("user ip = " + ip);

        if (CLIENTS.contains(session)) {

            System.out.println("[알림] 이미 연결된 세션입니다. > " + session);
        } else {

            CLIENTS.add(session);

            System.out.println("[알림] 새로운 세션입니다. ID > " + session.getId());

            session.getBasicRemote().sendText("안녕하세요 서버입니다. 연결을 축하해요.");
            session.getBasicRemote().sendText("당신의 아이디는 [ " + session.getId() + " ] 입니다.");

        }
        System.out.println();
    }

    // @OnClose > 사용자가 종료되면 session을 제거합니다.
    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        System.out.println("[알림] 세션을 닫습니다. : " + session.getId());
        System.out.println();
    }

    // @OnMessage > 사용자가 입력한 메세지를 받고 접속되어있는 사용자에게 메세지를 보냅니다.
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {

        String sessionId = session.getId();
        System.out.println("[알림] sessionId : " + sessionId + "이 메시지를 보냈습니다.");
        System.out.println("[알림] 입력된 메세지 > " + message);

        for (Session client : CLIENTS) {

            if(!Objects.equals(session.getId(), client.getId())) {
                System.out.println("[알림] Session Id : " + client.getId() + " 에게 메세지를 전달합니다. > " + message);
                client.getBasicRemote().sendText(sessionId + " 의 메시지 : " + message);
            }

            // 명령어 이벤트 전달 테스트트
           if(message.equals("a")) {
                client.getBasicRemote().sendText(message);
            }

        }
        System.out.println();
    }

    public String getClientAccessIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;

    }

}

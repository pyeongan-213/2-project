package kr.co.duck.websocket;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.duck.beans.WebSocketMessageBean;
import kr.co.duck.beans.WebSocketResponseBean;
import kr.co.duck.repository.SessionRepository;
import kr.co.duck.service.QuizRoomService;
import kr.co.duck.service.WebSocketService;

@Component
public class SignalHandler extends TextWebSocketHandler {

    private QuizRoomService quizRoomService;
    private WebSocketService webSocketService;
    private SessionRepository sessionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 생성

    private static final String MSG_TYPE_JOIN_ROOM = "join_room";
    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_CANDIDATE = "candidate";

    // **필드 주입을 setter 방식으로 변경해 순환 참조 해결**
    @Lazy
    public SignalHandler(QuizRoomService quizRoomService, WebSocketService webSocketService,
                         SessionRepository sessionRepository) {
        this.quizRoomService = quizRoomService;
        this.webSocketService = webSocketService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        System.out.println("WebSocket 연결이 수립되었습니다: " + session.getId());
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
        try {
            WebSocketMessageBean message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessageBean.class);
            String userName = message.getSender();
            Long roomId = message.getRoomId();

            switch (message.getType()) {
                case MSG_TYPE_JOIN_ROOM:
                    webSocketService.handleJoinRoom(session, message, roomId, userName);
                    break;

                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_CANDIDATE:
                    webSocketService.handleOfferAnswerCandidate(session, message, roomId);
                    break;

                default:
                    System.out.println("알 수 없는 메시지 유형입니다: " + message.getType());
                    break;
            }
        } catch (JsonProcessingException e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        System.out.println("WebSocket 연결이 종료되었습니다: " + session.getId());
        webSocketService.handleDisconnect(session);
    }

    private void sendMessage(final WebSocketSession session, final WebSocketResponseBean message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            System.err.println("메시지 발송 오류: " + e.getMessage());
        }
    }
}

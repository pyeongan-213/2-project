package kr.co.duck.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import kr.co.duck.domain.ChatMessage;
import kr.co.duck.domain.QuizMessage;

// 기능: 메시지 제어 및 카메라 제어
@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final SimpMessageSendingOperations sendingOperations;

    // 생성자
    public ChatService(SimpMessageSendingOperations sendingOperations) {
        this.sendingOperations = sendingOperations;
    }

    // 메시지 제어
    public void message(ChatMessage message) {
        ChatMessage exportMessage = new ChatMessage(); // ChatMessage 객체 생성

        exportMessage.setType(message.getType());
        exportMessage.setSender(message.getSender());
        exportMessage.setMessage(message.getMessage());

        sendingOperations.convertAndSend("/sub/gameRoom/" + message.getRoomId(), exportMessage);
        log.info("Message sent: {}", exportMessage); // 로그 추가
    }

    // 카메라 제어 (카메라를 끈 유저를 알기 위한 API)
    public void cameraControl(ChatMessage message) {
        ChatMessage exportMessage = new ChatMessage(); // ChatMessage 객체 생성

        exportMessage.setType(message.getType());
        exportMessage.setNickname(message.getNickname());

        sendingOperations.convertAndSend("/sub/gameRoom/" + message.getRoomId(), exportMessage);
        log.info("Camera control message sent: {}", exportMessage); // 로그 추가
    }

    // 퀴즈 메시지 제어
    public <T> void sendQuizMessage(int roomId, QuizMessage.MessageType messageType, T content, String sender) {
        String senderName = sender == null ? "관리자" : sender;

        QuizMessage<T> quizMessage = new QuizMessage<>();
        quizMessage.setQuizRoomId(roomId);
        quizMessage.setType(messageType);
        quizMessage.setSender(senderName);
        quizMessage.setContent(content);

        sendingOperations.convertAndSend("/sub/quizRoom/" + roomId, quizMessage);
        log.info("Quiz message sent: {}", quizMessage); // 로그 추가
    }
}

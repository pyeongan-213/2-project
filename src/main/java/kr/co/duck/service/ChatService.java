package kr.co.duck.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import kr.co.duck.domain.ChatMessage;
import kr.co.duck.domain.QuizMessage;

/**
 * 기능: 채팅 메시지와 퀴즈 메시지 전송 관리
 */
@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final SimpMessageSendingOperations sendingOperations;

    // 생성자
    public ChatService(SimpMessageSendingOperations sendingOperations) {
        this.sendingOperations = sendingOperations;
    }

    /**
     * 일반 채팅 메시지 전송 메서드
     * @param roomId 방 ID
     * @param sender 메시지 보낸 사람 (닉네임)
     * @param content 메시지 내용
     */
    public void sendChatMessage(int roomId, String sender, String content) {
        ChatMessage<String> chatMessage = new ChatMessage<>();
        chatMessage.setRoomId(String.valueOf(roomId));  // roomId를 String으로 변환
        chatMessage.setSender(sender);
        chatMessage.setMessage(content);

        sendingOperations.convertAndSend("/sub/chatRoom/" + roomId, chatMessage);
        log.info("Chat message sent: {}", chatMessage);
    }

    /**
     * 카메라 제어 메시지 전송 메서드
     * @param message 카메라 제어 메시지
     */
    public <T> void cameraControl(ChatMessage<T> message) {
        ChatMessage<T> exportMessage = new ChatMessage<>();
        exportMessage.setType(message.getType());
        exportMessage.setNickname(message.getNickname());

        sendingOperations.convertAndSend("/sub/gameRoom/" + message.getRoomId(), exportMessage);
        log.info("Camera control message sent: {}", exportMessage);
    }

    /**
     * 메시지 전송 메서드 (일반 메시지 전송)
     * @param message ChatMessage 객체
     */
    public <T> void message(ChatMessage<T> message) {
        log.info("Processing chat message: {}", message);

        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }

    /**
     * 퀴즈 메시지 전송 메서드
     * @param roomId 방 ID
     * @param messageType 메시지 유형 (예: START, CORRECT, INCORRECT 등)
     * @param content 메시지 내용 (정답 여부 등)
     * @param sender 메시지 보낸 사람 (관리자 또는 참여자)
     */
    public <T> void sendQuizMessage(int roomId, QuizMessage.MessageType messageType, T content, String sender) {
        String senderName = sender == null ? "관리자" : sender;

        QuizMessage<T> quizMessage = new QuizMessage<>();
        quizMessage.setQuizRoomId(roomId);
        quizMessage.setType(messageType);
        quizMessage.setSender(senderName);
        quizMessage.setContent(content);

        sendingOperations.convertAndSend("/sub/quizRoom/" + roomId, quizMessage);
        log.info("Quiz message sent: {}", quizMessage);
    }

    /**
     * 정답 메시지 전송 메서드
     * @param roomId 방 ID
     * @param memberId 정답을 제출한 멤버 ID
     * @param isCorrect 정답 여부 (true: 정답, false: 오답)
     */
    public void sendAnswerMessage(int roomId, int memberId, boolean isCorrect) {
        // 정답 메시지 생성
        String messageContent = isCorrect ? "정답입니다!" : "오답입니다!";
        QuizMessage<String> answerMessage = new QuizMessage<>();
        answerMessage.setQuizRoomId(roomId);
        answerMessage.setType(isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT);
        answerMessage.setSender(String.valueOf(memberId));
        answerMessage.setContent(messageContent);

        // WebSocket을 통해 전송
        sendingOperations.convertAndSend("/sub/quizRoom/" + roomId, answerMessage);
        log.info("Answer message sent: {}", answerMessage);
    }
}
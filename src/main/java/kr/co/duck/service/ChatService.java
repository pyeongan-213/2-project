package kr.co.duck.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.duck.domain.Chat;
import kr.co.duck.domain.ChatMessage;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.repository.ChatRepository;

/**
 * 기능: 채팅 메시지와 퀴즈 메시지 전송 관리
 */
@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final SimpMessageSendingOperations sendingOperations;
    private QuizService quizService; // Setter로 주입

    public ChatService(SimpMessageSendingOperations sendingOperations) {
        this.sendingOperations = sendingOperations;
    }

    @Autowired
    private ChatRepository chatRepository;

    
    @Autowired
    public void setQuizService(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * 일반 채팅 메시지 전송 메서드
     * @param roomId 방 ID
     * @param sender 메시지 보낸 사람 (닉네임)
     * @param content 메시지 내용
     */
    public void sendChatMessage(int roomId, String sender, String content) {
        if (content.equalsIgnoreCase("!힌트")) {
            sendHintMessage(); // 힌트 메시지 전송
            return;
        }

        if (content.equalsIgnoreCase("!스킵")) {
            skipQuiz(roomId, sender); // 스킵 처리
            return;
        }

        ChatMessage<String> chatMessage = new ChatMessage<>();
        chatMessage.setRoomId(String.valueOf(roomId));
        chatMessage.setSender(sender);
        chatMessage.setMessage(content);

        sendingOperations.convertAndSend("/sub/chatRoom/" + roomId, chatMessage);
        log.info("Chat message sent: {}", chatMessage);
    }

    /**
     * 힌트 메시지 전송 메서드
     */
    private void sendHintMessage() {
        QuizMessage<String> hintMessage = new QuizMessage<>();
        hintMessage.setType(QuizMessage.MessageType.HINT);
        hintMessage.setSender("시스템");
        hintMessage.setContent("힌트가 제공되었습니다.");

        sendingOperations.convertAndSend("/sub/quizRoom/1", hintMessage); // Room ID 1 예시
        log.info("Hint message sent.");
    }

    /**
     * 스킵 명령 처리 메서드
     * @param roomId 방 ID
     * @param sender 명령을 입력한 사용자
     */
    private void skipQuiz(int roomId, String sender) {
        // 스킵 알림 메시지 전송
        sendingOperations.convertAndSend("/sub/quizRoom/" + roomId, 
            new QuizMessage<>("시스템", sender + "님이 문제를 스킵했습니다. 다음 문제로 이동합니다."));

        // 다음 퀴즈 시작
        quizService.startNextQuiz(roomId);
        log.info("Quiz skipped by: {}", sender);
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
        String messageContent = isCorrect ? "정답입니다!" : "오답입니다!";
        QuizMessage<String> answerMessage = new QuizMessage<>();
        answerMessage.setQuizRoomId(roomId);
        answerMessage.setType(isCorrect ? QuizMessage.MessageType.CORRECT : QuizMessage.MessageType.INCORRECT);
        answerMessage.setSender(String.valueOf(memberId));
        answerMessage.setContent(messageContent);

        sendingOperations.convertAndSend("/sub/quizRoom/" + roomId, answerMessage);
        log.info("Answer message sent: {}", answerMessage);
    }
    

    public void saveChatMessage(int room_Id, int member_Id, String content) {
        log.info("채팅 저장 시도 - roomId: {}, memberId: {}, content: {}", room_Id, member_Id, content);

        try {
            Chat chat = new Chat();
            chat.setRoom_Id(room_Id);
            chat.setMember_Id(member_Id);
            chat.setChat_Text(content);
            chat.setChat_Time(LocalDateTime.now().toString());

            log.info("저장할 채팅 엔티티: {}", chat);

            chatRepository.save(chat);
            log.info("채팅 저장 성공");
        } catch (Exception e) {
            log.error("채팅 저장 중 오류 발생: {}", e.getMessage(), e);
            throw e; // 예외를 던져 디버깅 시 더 상세한 스택 트레이스 확인 가능
        }
    }

}

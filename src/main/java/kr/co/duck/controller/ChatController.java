package kr.co.duck.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import kr.co.duck.domain.ChatMessage;
import kr.co.duck.service.ChatService;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 생성자 주입
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    // STOMP 채팅 메시지 처리 (제네릭 타입 명시: ChatMessage<String>)
    @MessageMapping("/chat/{roomId}")
    public void message(@DestinationVariable int roomId, ChatMessage<String> message) {
        log.info("Received chat message for room {}: {}", roomId, message);
        
        // 채팅 서비스 처리 호출
        chatService.message(message);

        // 받은 메시지를 구독 경로로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/" + roomId, message);
    }

    // 카메라 제어 메시지 처리 (제네릭 타입 명시: ChatMessage<String>)
    @MessageMapping("/chat/camera/{roomId}")
    public void cameraControl(@DestinationVariable int roomId, ChatMessage<String> message) {
        log.info("Received camera control message for room {}: {}", roomId, message);

        // 카메라 제어 서비스 호출
        chatService.cameraControl(message);

        // 제어 상태를 구독 경로로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/camera/" + roomId, message);
    }
    
    
    @PostMapping("/saveChat")
    public ResponseEntity<?> saveChatMessage(@RequestBody Map<String, Object> chatData) {
        try {
        	
            int roomId = (int) chatData.get("roomId");
            int memberId = (int) chatData.get("memberId");
            String content = (String) chatData.get("content");

            // 로그 출력 (chatData의 값들을 추출하여 출력)
            log.debug("채팅 데이터 수신: roomId = {}, memberId = {}, chatText = {}", roomId, memberId, content);

            // 채팅 메시지 저장
            chatService.saveChatMessage(roomId, memberId, content);

            return ResponseEntity.ok("채팅 메시지가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("채팅 저장 중 오류 발생: " + e.getMessage());
        }
    }
}

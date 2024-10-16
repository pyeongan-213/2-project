package kr.co.duck.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @MessageMapping("/chat/message")
    public void message(ChatMessage<String> message) {
        log.info("Received chat message: {}", message);
        
        // 채팅 서비스 처리 호출
        chatService.message(message);

        // 받은 메시지를 구독 경로로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }

    // 카메라 제어 메시지 처리 (제네릭 타입 명시: ChatMessage<String>)
    @MessageMapping("/chat/camera")
    public void cameraControl(ChatMessage<String> message) {
        log.info("Received camera control message: {}", message);

        // 카메라 제어 서비스 호출
        chatService.cameraControl(message);

        // 제어 상태를 구독 경로로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/camera/" + message.getRoomId(), message);
    }
}

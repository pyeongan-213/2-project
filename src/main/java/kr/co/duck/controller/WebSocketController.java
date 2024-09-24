package kr.co.duck.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messagingTemplate;

	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@PostMapping("/send")
	public void sendMessage(String message) {
		messagingTemplate.convertAndSend("/topic/messages", message);
	}
}

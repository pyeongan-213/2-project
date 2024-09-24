package kr.co.duck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.duck.beans.RoomBean;
import kr.co.duck.service.RoomService;

@Controller
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송용

    // 로비 페이지로 이동 (방 목록 표시)
    @GetMapping
    public String getLobby(Model model) {
        List<RoomBean> roomList = roomService.getRoomList();
        model.addAttribute("rooms", roomList);
        return "quiz/lobby"; // lobby.jsp로 이동
    }

    // 방 생성 요청 처리
    @PostMapping("/create")
    public String createRoom(RoomBean room) {
		roomService.createRoom(room);
		messagingTemplate.convertAndSend("/topic/roomList", roomService.getRoomList()); // 모든 클라이언트에게 방 목록 업데이트
		return "redirect:/lobby";
	}

	// 방 목록 실시간 갱신
	@GetMapping("/rooms")
	public String refreshRoomList(Model model) {
		List<RoomBean> roomList = roomService.getRoomList();
		model.addAttribute("rooms", roomList);
		return "quiz/lobby :: #room-list"; // 방 목록 부분만 갱신
	}
}

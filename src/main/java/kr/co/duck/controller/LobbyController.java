package kr.co.duck.controller;

import kr.co.duck.beans.QuizRoomListBean;
import kr.co.duck.service.QuizRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/quiz")
public class LobbyController {

	private final QuizRoomService quizRoomService;

	@Autowired
	public LobbyController(QuizRoomService quizRoomService) {
		this.quizRoomService = quizRoomService;
	}

	// 로비 페이지로 이동
	@GetMapping("/quizlobby")
	public String showLobby(Model model) {
		// 퀴즈방 목록을 가져와 모델에 추가
		QuizRoomListBean quizRooms = quizRoomService.getAllQuizRooms(null);
		model.addAttribute("rooms", quizRooms.getQuizRoomBeanList());

		return "quiz/quizlobby"; // quizlobby.jsp로 이동
	}

	// 방 목록을 업데이트하기 위한 API
	@GetMapping("/lobby/rooms")
	@ResponseBody
	public QuizRoomListBean getRooms() {
		return quizRoomService.getAllQuizRooms(null);
	}

}

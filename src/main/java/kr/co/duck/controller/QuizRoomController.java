package kr.co.duck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.duck.beans.QuizRoomListBean;
import kr.co.duck.service.QuizRoomService;

@RestController
public class QuizRoomController {

	private final QuizRoomService quizRoomService;

	@Autowired
	public QuizRoomController(QuizRoomService quizRoomService) {
		this.quizRoomService = quizRoomService;
	}

	// 퀴즈방 전체 조회 API
	@GetMapping("/quizrooms")
	public ResponseEntity<QuizRoomListBean> getAllQuizRooms(Pageable pageable) {
		QuizRoomListBean quizRooms = quizRoomService.getAllQuizRooms(pageable);
		return ResponseEntity.ok(quizRooms);
	}

	// 퀴즈방 키워드 검색 API
	@GetMapping("/quizrooms/search")
	public ResponseEntity<QuizRoomListBean> searchQuizRooms(Pageable pageable, @RequestParam String keyword) {
		QuizRoomListBean quizRooms = quizRoomService.searchQuizRoom(pageable, keyword);
		return ResponseEntity.ok(quizRooms);
	}
}

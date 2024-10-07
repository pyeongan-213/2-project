package kr.co.duck.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.QuizRoomBean;
import kr.co.duck.beans.QuizRoomListBean;
import kr.co.duck.service.QuizRoomService;
import kr.co.duck.util.CustomException;

@Controller
@RequestMapping("/quiz/rooms")
public class QuizRoomController {

	private final QuizRoomService quizRoomService;

	@Autowired
	public QuizRoomController(QuizRoomService quizRoomService) {
		this.quizRoomService = quizRoomService;
	}

	// 퀴즈방 전체 조회 API
	@GetMapping
	public ResponseEntity<QuizRoomListBean> getAllQuizRooms(Pageable pageable) {
		QuizRoomListBean quizRooms = quizRoomService.getAllQuizRooms(pageable);
		return ResponseEntity.ok(quizRooms);
	}

	// 퀴즈방 키워드 검색 API
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchQuizRooms(Pageable pageable,
			@RequestParam(required = false) String keyword) {
		Map<String, Object> response = new HashMap<>();
		if (keyword == null || keyword.isEmpty()) {
			response.put("success", false);
			response.put("message", "검색 키워드가 필요합니다.");
			return ResponseEntity.badRequest().body(response);
		}

		QuizRoomListBean quizRooms = quizRoomService.searchQuizRoom(pageable, keyword);
		response.put("success", true);
		response.put("data", quizRooms);
		response.put("message", quizRooms.getQuizRoomBeanList().isEmpty() ? "검색 결과가 없습니다." : "검색에 성공했습니다.");

		return ResponseEntity.ok(response);
	}

	// 퀴즈방 상세 조회
	@GetMapping("/{roomId}")
	public String getQuizRoom(@PathVariable int roomId, Model model) {
		try {
			QuizRoomBean quizRoom = quizRoomService.findRoomById(roomId);
			model.addAttribute("room", quizRoom);
			return "quiz/quizRoom"; // quizRoom.jsp로 이동
		} catch (Exception e) {
			model.addAttribute("errorMessage", "방 조회 중 오류가 발생했습니다: " + e.getMessage());
			return "errorPage"; // 에러가 발생하면 에러 페이지로 이동
		}
	}

	// 퀴즈방 생성 API
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createRoom(@RequestBody QuizRoomBean quizRoomBean, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

		if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
			response.put("success", false);
			response.put("message", "로그인 후 사용해 주세요.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {
			QuizRoomBean createdRoom = quizRoomService.createRoom(quizRoomBean, loginMemberBean);
			response.put("success", true);
			response.put("roomId", createdRoom.getQuizRoomId());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "방 생성에 실패했습니다: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 퀴즈방 참여시
	@PostMapping("/join")
	public ResponseEntity<Map<String, Object>> joinRoom(@RequestBody Map<String, Object> requestData,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

		// 로그인 검증
		if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
			response.put("success", false);
			response.put("message", "로그인 후 사용해 주세요.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {
			// 전체 요청 데이터 출력
			System.out.println("Request Data: " + requestData);

			// roomId 유효성 검사
			if (!requestData.containsKey("roomId")) {
				response.put("success", false);
				response.put("message", "잘못된 요청 데이터입니다.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			int roomId = Integer.parseInt(requestData.get("roomId").toString());
			String roomPassword = (String) requestData.getOrDefault("roomPassword", "");

			System.out.println("Received roomId: " + roomId);
			System.out.println("Received roomPassword: " + roomPassword);

			// 중복된 사용자 방 참여 방지 로직은 QuizRoomService의 enterQuizRoom에서 처리
			quizRoomService.enterQuizRoom(roomId, loginMemberBean, roomPassword);
			response.put("success", true);
			response.put("roomId", roomId);
			return ResponseEntity.ok(response);
		} catch (CustomException e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/leave")
	public ResponseEntity<Map<String, Object>> leaveRoom(@RequestBody Map<String, Object> requestData,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

		// 세션에 로그인 정보가 없을 때의 처리
		if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
			response.put("success", false);
			response.put("message", "로그인 후 사용해 주세요.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {
			int roomId = Integer.parseInt(requestData.get("roomId").toString());
			System.out.println("Received roomId for leave: " + roomId);

			quizRoomService.roomExit(roomId, loginMemberBean);
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace(); // 예외 스택 추적을 로그에 출력
			response.put("success", false);
			response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
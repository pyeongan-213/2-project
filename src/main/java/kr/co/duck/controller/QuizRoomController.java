package kr.co.duck.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.beans.QuizRoomBean;
import kr.co.duck.beans.QuizRoomListBean;
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.service.QuizRoomService;
import kr.co.duck.service.QuizService;
import kr.co.duck.util.CustomException;

@Controller
@RequestMapping("/quiz/rooms")
public class QuizRoomController {

    private final QuizRoomService quizRoomService;
    private final QuizService quizService;

    @Autowired
    public QuizRoomController(QuizRoomService quizRoomService, QuizService quizService) {
        this.quizRoomService = quizRoomService;
        this.quizService = quizService;
    }

    // **퀴즈방 전체 조회 API**
    @GetMapping
    public ResponseEntity<QuizRoomListBean> getAllQuizRooms(Pageable pageable) {
        QuizRoomListBean quizRooms = quizRoomService.getAllQuizRooms(pageable);
        return ResponseEntity.ok(quizRooms);
    }

    // **퀴즈방 키워드 검색 API**
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

    // **퀴즈방 상세 조회 API**
    @GetMapping("/{roomId}")
    public String getQuizRoom(@PathVariable int roomId, Model model) {
        try {
            QuizRoomBean quizRoom = quizRoomService.findRoomById(roomId);
            model.addAttribute("room", quizRoom);
            model.addAttribute("quizQuestionType", quizRoom.getQuizRoomType());

            return "quiz/quizRoom"; // quizRoom.jsp로 이동
        } catch (CustomException e) {
            model.addAttribute("errorMessage", "방 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "errorPage"; // 에러가 발생하면 에러 페이지로 이동
        }
    }

    // **퀴즈방 생성 API**
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody QuizRoomBean quizRoomBean, HttpSession session) {
    	 System.out.println("[INFO] 방 생성 요청: " + quizRoomBean);  // 생성된 방 정보 로그
    	Map<String, Object> response = new HashMap<>();
        MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

        if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
            response.put("success", false);
            response.put("message", "로그인 후 사용해 주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int maxParticipants = quizRoomBean.getMaxCapacity();
        int maxSongs = quizRoomBean.getMaxMusic();

        // 최대 인원 및 최대 곡수 검증
        if (maxParticipants >= 10) {
            response.put("success", false);
            response.put("message", "최대 인원은 10명까지 가능합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (maxSongs != 100 && maxSongs != 200 && maxSongs != 300 &&
            maxSongs != 400 && maxSongs != 500) {
            response.put("success", false);
            response.put("message", "최대 곡수는 100, 200, 300, 400, 500 중 하나여야 합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            QuizRoomBean createdRoom = quizRoomService.createRoom(quizRoomBean, loginMemberBean);
            response.put("success", true);
            response.put("roomId", createdRoom.getQuizRoomId());
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            response.put("success", false);
            response.put("message", "방 생성에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(response);
        }
    }


    // **퀴즈방 참여 API**
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinRoom(@RequestBody Map<String, Object> requestData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

        if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
            response.put("success", false);
            response.put("message", "로그인 후 사용해 주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            int roomId = Integer.parseInt(requestData.get("roomId").toString());
            String roomPassword = (String) requestData.getOrDefault("roomPassword", "");

            quizRoomService.enterQuizRoom(roomId, loginMemberBean, roomPassword);
            response.put("success", true);
            response.put("roomId", roomId);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(response);
        }
    }

    // **플레이어 닉네임 받아오는 API**
    @GetMapping("/{roomId}/players")
    public ResponseEntity<Map<String, Object>> getRoomPlayers(@PathVariable int roomId) {
        try {
            List<String> playerNicknames = quizRoomService.getAttendeesNicknamesByRoomId(roomId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("players", playerNicknames);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{roomId}/start")
    public ResponseEntity<Map<String, Object>> quizStart(@PathVariable int roomId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 방 ID로 퀴즈 유형 가져오기
            String quizType = quizService.getQuizTypeForRoom(roomId);

            // 방 ID를 사용해 랜덤 퀴즈 가져오기 (quizId를 1로 가정)
            QuizMusic quiz = quizService.getRandomQuizQuestion(1, quizType); 

            response.put("success", true);
            response.put("quiz", quiz);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "퀴즈를 가져오는 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    // **퀴즈방 나가기 API**
    @PostMapping("/leave")
    public ResponseEntity<Map<String, Object>> leaveRoom(@RequestBody Map<String, Object> requestData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");

        if (loginMemberBean == null || !loginMemberBean.isMemberLogin()) {
            response.put("success", false);
            response.put("message", "로그인 후 사용해 주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            int roomId = Integer.parseInt(requestData.get("roomId").toString());
            quizRoomService.roomExit(roomId, loginMemberBean);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

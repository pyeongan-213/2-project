package kr.co.duck.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.duck.domain.QuizMusic;
import kr.co.duck.service.ChatService;
import kr.co.duck.service.QuizService;
import kr.co.duck.util.CustomException;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService; // 퀴즈 서비스 객체
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송 템플릿(WebSocket)
    
    @Autowired
    private ChatService chatService;
    
    // **생성자 주입을 통한 의존성 주입**
    public QuizController(QuizService quizService, SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.quizService = quizService;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    // **퀴즈 생성: QuizMusic 객체를 저장**
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizMusic quizMusic) {
        quizService.createQuiz(quizMusic); // 퀴즈 저장
        return ResponseEntity.ok("퀴즈가 생성되었습니다."); // 응답 반환
    }

    // **모든 퀴즈 목록 가져오기**
    @GetMapping("/list")
    public ResponseEntity<List<QuizMusic>> getAllQuizzes() {
        List<QuizMusic> quizzes = quizService.getAllQuizzes(); // 모든 퀴즈 가져오기
        return ResponseEntity.ok(quizzes); // 응답으로 퀴즈 목록 반환
    }

    // **특정 퀴즈의 모든 문제를 가져오기**
    @GetMapping("/details/{quizId}")
    public ResponseEntity<List<QuizMusic>> getQuizByQuizId(@PathVariable int quizId) {
        List<QuizMusic> quizList = quizService.getQuizByQuizId(quizId); // 특정 퀴즈 문제 조회
        return ResponseEntity.ok(quizList); // 문제 목록 반환
    }

 // **랜덤 퀴즈 시작: 방 ID와 연관된 퀴즈 문제 가져오기**
    @GetMapping("/rooms/{roomId}/random")
    public ResponseEntity<?> quizStart(@PathVariable int roomId) {
       // System.out.println("[INFO] 랜덤 퀴즈 요청: roomId = " + roomId); // 로그 추가

        try {
            // **퀴즈 유형 가져오기 (예: songTitle 또는 artistName)**
            String quizType = quizService.getQuizTypeForRoom(roomId); 
            //System.out.println("[INFO] 퀴즈 유형: " + quizType); // 로그 추가

            // **랜덤 퀴즈 문제 가져오기**
            QuizMusic quiz = quizService.getRandomQuizQuestion(roomId, quizType);
            //System.out.println("[INFO] 선택된 퀴즈: " + quiz); // 로그 추가

            // **WebSocket을 통해 퀴즈 문제 전송**
            messagingTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);
            //System.out.println("[INFO] 퀴즈 시작 메시지 전송 완료: roomId = " + roomId); // 로그 추가

            // **퀴즈 문제를 JSON 응답으로 반환**
            Map<String, Object> response = new HashMap<>();
            response.put("quiz", quiz);
            response.put("quizType", quizType); // 퀴즈 유형 포함

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            //System.err.println("[ERROR] CustomException 발생: " + e.getMessage()); // 로그 추가
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            //System.err.println("[ERROR] 예상치 못한 오류 발생: " + e.getMessage()); // 로그 추가
            e.printStackTrace(); // 전체 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("퀴즈를 가져오는 중 오류가 발생했습니다.");
        }
    }

    // **사용자 정답 제출 처리**
    @PostMapping("/answer/{memberId}")
    public ResponseEntity<String> submitAnswer(
            @PathVariable int memberId,
            @RequestParam int quizId,
            @RequestParam String answer) {

        // 사용자가 제출한 정답과 DB 정답 비교
        boolean isCorrect = quizService.submitAnswer(memberId, quizId, answer.trim());

        if (isCorrect) {
            // 정답 메시지 생성
            Map<String, Object> answerMessage = new HashMap<>();
            answerMessage.put("isCorrect", true); // 정답 여부
            answerMessage.put("playerId", memberId); // 정답자 ID
            answerMessage.put("message", "정답입니다!");

            // WebSocket을 통해 정답 메시지 전송
            messagingTemplate.convertAndSend("/sub/quiz/answer/" + quizId, answerMessage);

            // 정답 응답 반환
            return ResponseEntity.ok("정답입니다!");
        } else {
            return ResponseEntity.ok(""); // 오답일 경우 빈 응답 반환
        }
    }

    // **퀴즈 삭제**
    @DeleteMapping("/delete/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId); // 퀴즈 삭제
        return ResponseEntity.ok("퀴즈가 삭제되었습니다."); // 응답 반환
    }
}

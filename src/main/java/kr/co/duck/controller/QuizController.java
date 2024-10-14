package kr.co.duck.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import kr.co.duck.service.QuizService;
import kr.co.duck.util.CustomException;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final SimpMessagingTemplate messagingTemplate;

    // **생성자 주입**
    public QuizController(QuizService quizService, SimpMessagingTemplate messagingTemplate) {
        this.quizService = quizService;
        this.messagingTemplate = messagingTemplate;
    }

    // **퀴즈 생성 (QuizMusic 저장)**
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizMusic quizMusic) {
        quizService.createQuiz(quizMusic);
        return ResponseEntity.ok("퀴즈가 생성되었습니다.");
    }

    // **모든 퀴즈 목록 가져오기**
    @GetMapping("/list")
    public ResponseEntity<List<QuizMusic>> getAllQuizzes() {
        List<QuizMusic> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    // **특정 퀴즈의 모든 문제 가져오기**
    @GetMapping("/details/{quizId}")
    public ResponseEntity<List<QuizMusic>> getQuizByQuizId(@PathVariable int quizId) {
        List<QuizMusic> quizList = quizService.getQuizByQuizId(quizId);
        return ResponseEntity.ok(quizList);
    }
    
    // **퀴즈를 랜덤으로 뽑아옴**
    @GetMapping("/rooms/{roomId}/random")
    public ResponseEntity<?> quizStart(@PathVariable int roomId) {
        try {
            System.out.println("퀴즈 시작 요청: roomId = " + roomId); // 로그 추가
            QuizMusic quiz = quizService.getRandomQuizQuestion(1); // quiz_id를 1로 명시

            System.out.println("조회된 랜덤 퀴즈: " + quiz); // 확인용 로그

            if (quiz == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("퀴즈를 찾을 수 없습니다.");
            }

            // WebSocket 메시지 전송
            messagingTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

            // JSON 응답 반환		
            return ResponseEntity.ok(quiz);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("퀴즈를 가져오는 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/answer/{memberId}")
    public ResponseEntity<String> submitAnswer(
            @PathVariable int memberId,
            @RequestParam int quizId,
            @RequestParam String answer) {

        // DB에 저장된 정답과 사용자의 입력 비교
        boolean isCorrect = quizService.submitAnswer(memberId, quizId, answer.trim());

        // 정답 메시지 생성
        if (isCorrect) {
            Map<String, Object> answerMessage = new HashMap<>();
            answerMessage.put("isCorrect", true); // 정답 여부
            answerMessage.put("playerId", memberId); // 정답자 ID
            answerMessage.put("message", "정답입니다!");

            // WebSocket을 통해 정답 메시지 전송
            messagingTemplate.convertAndSend("/sub/quiz/answer/" + quizId, answerMessage);
            
            return ResponseEntity.ok("정답입니다!"); // 정답 응답 반환
        } else {
            return ResponseEntity.ok(""); // 오답일 경우 빈 응답 반환
        }
    }


    // **퀴즈 삭제**
    @DeleteMapping("/delete/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok("퀴즈가 삭제되었습니다.");
    }
} 
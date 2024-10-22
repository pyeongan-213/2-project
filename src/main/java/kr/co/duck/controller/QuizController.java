package kr.co.duck.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.duck.domain.CorrectAnswerMessage;
import kr.co.duck.domain.QuizMusic;
import kr.co.duck.service.ChatService;
import kr.co.duck.service.QuizService;
import kr.co.duck.util.CustomException;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService; // 퀴즈 서비스 객체
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송 템플릿(WebSocket)
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatService chatService;
    
    private SimpMessagingTemplate simpMessagingTemplate;
    
    // **생성자 주입을 통한 의존성 주입**
    public QuizController(QuizService quizService, SimpMessagingTemplate messagingTemplate, ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
        this.quizService = quizService;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
       //System.out.println("[INFO] 랜덤 퀴즈 요청: roomId = " + roomId); // 로그 추가

        try {
            //**퀴즈 유형 가져오기 (예: songTitle 또는 artistName)**
            String quizType = quizService.getQuizTypeForRoom(roomId); 
           // System.out.println("[INFO] 퀴즈 유형: " + quizType); // 로그 추가

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

    // **정답 제출 및 정답자 정보 전송**
    
    
    @MessageMapping("/quiz/{roomId}/correctAnswer")
    public void sendCorrectAnswer(CorrectAnswerMessage message, @DestinationVariable String roomId) {
    	  // 방에 있는 모든 사용자에게 정답자와 타이머 정보를 전송
        message.setTimer(10);  // 타이머를 10초로 설정
        simpMessagingTemplate.convertAndSend("/sub/quizRoom/" + roomId + "/correctAnswer", message);
    }

    
 // **힌트 전송**
    @MessageMapping("/quiz/{roomId}/hintMessage")
    @SendTo("/sub/quiz/{roomId}/hintMessage")
    public ResponseEntity<String> sendHint(@DestinationVariable int roomId) {
        try {
            // 퀴즈 ID를 기준으로 힌트 생성
            String hint = quizService.generateHint(roomId); // roomId는 quizId로 사용

            // **힌트를 모든 참가자에게 WebSocket을 통해 전송**
            Map<String, Object> hintMessage = new HashMap<>();
            hintMessage.put("hint", hint);
            System.out.println("힌트 메시지 전송: " + hint);
            messagingTemplate.convertAndSend("/sub/quiz/" + roomId + "/hintMessage", hintMessage);
            
            return ResponseEntity.ok("힌트가 전송되었습니다.");
       
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("힌트를 생성할 수 없습니다.");
        }
    }
    
    //타이머
    @MessageMapping("/quiz/{roomId}/timer")
    @SendTo("/sub/quiz/{roomId}/timer")
    public ResponseEntity<String> startTimer(@DestinationVariable int roomId, @RequestParam int duration) {
        // 타이머 시작 정보를 WebSocket으로 전송
        Map<String, Object> timerMessage = new HashMap<>();
        timerMessage.put("timeLeft", duration);
        messagingTemplate.convertAndSend("/sub/quiz/" + roomId + "/timer", timerMessage);

        return ResponseEntity.ok("타이머가 시작되었습니다.");
    }

    
    
    // **퀴즈 삭제**
    @DeleteMapping("/delete/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId); // 퀴즈 삭제
        return ResponseEntity.ok("퀴즈가 삭제되었습니다."); // 응답 반환
    }
}

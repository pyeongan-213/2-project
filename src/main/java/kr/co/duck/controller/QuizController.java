package kr.co.duck.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import kr.co.duck.beans.QuizBean;
import kr.co.duck.service.QuizService;
import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	private final QuizService quizService;
	private final SimpMessagingTemplate messagingTemplate; // WebSocket 템플릿 추가

	public QuizController(QuizService quizService, SimpMessagingTemplate messagingTemplate) {
		this.quizService = quizService;
		this.messagingTemplate = messagingTemplate;
	}

	@PostMapping("/create")
	public String createQuiz(@RequestBody QuizBean quizBean) {
		quizService.createQuiz(quizBean);
		return "퀴즈가 생성되었습니다.";
	}

	@GetMapping("/list")
	public List<QuizBean> getAllQuizzes() {
		return quizService.getAllQuizzes();
	}

	@GetMapping("/details/{quizId}")
	public QuizBean getQuiz(@PathVariable String quizId) {
	    try {
	        int id = Integer.parseInt(quizId);
	        return quizService.getQuiz(id);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("잘못된 퀴즈 ID입니다: " + quizId);
	    }
	}

	@PutMapping("/update/{quizId}")
	public String updateQuiz(@PathVariable int quizId, @RequestBody QuizBean quizBean) {
		quizService.updateQuiz(quizId, quizBean);
		return "퀴즈가 업데이트되었습니다.";
	}

	@DeleteMapping("/delete/{quizId}")
	public String deleteQuiz(@PathVariable int quizId) {
		quizService.deleteQuiz(quizId);
		return "퀴즈가 삭제되었습니다.";
	}

	// 퀴즈 시작 (WebSocket 사용)
	@PostMapping("/start/{roomId}")
	public String quizStart(@PathVariable int roomId) {
		// 퀴즈 시작 로직 처리
		quizService.quizStart(roomId, new QuizBean());

		// WebSocket 메시지 전송 (퀴즈 시작)
		messagingTemplate.convertAndSend("/sub/quiz/" + roomId, "퀴즈가 시작되었습니다!");

		return "퀴즈가 시작되었습니다!";
	}

	// 정답 제출 메서드 (WebSocket 사용 가능)
	@PostMapping("/answer/{memberId}")
	public String submitAnswer(@PathVariable int memberId, @RequestBody QuizBean quizBean) {
		boolean isCorrect = quizService.submitAnswer(memberId, quizBean);

		// WebSocket을 통해 실시간 정답 여부 전송
		messagingTemplate.convertAndSend("/sub/quiz/answer/" + quizBean.getQuizId(), isCorrect);

		return isCorrect ? "정답입니다!" : "오답입니다!";
	}
}

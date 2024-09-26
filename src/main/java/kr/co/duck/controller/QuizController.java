package kr.co.duck.controller;

import org.springframework.web.bind.annotation.*;
import kr.co.duck.beans.QuizBean;
import kr.co.duck.service.QuizService;
import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	private final QuizService quizService;

	public QuizController(QuizService quizService) {
		this.quizService = quizService;
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

	@GetMapping("/{quizId}")
	public QuizBean getQuiz(@PathVariable int quizId) {
		return quizService.getQuiz(quizId);
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

	@PostMapping("/answer")
	public String submitAnswer(@RequestBody QuizBean quizBean) {
		boolean isCorrect = quizService.submitAnswer(quizBean);
		return isCorrect ? "정답입니다!" : "오답입니다!";
	}
}

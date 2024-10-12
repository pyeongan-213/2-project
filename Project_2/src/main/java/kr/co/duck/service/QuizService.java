package kr.co.duck.service;

import static kr.co.duck.util.StatusCode.QUIZ_NOT_FOUND;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.duck.beans.QuizBean;
import kr.co.duck.domain.QuizMessage;
import kr.co.duck.repository.QuizRepository;
import kr.co.duck.util.CustomException;

@Service
public class QuizService {

	private final QuizRepository quizRepository;

	// 생성자
	public QuizService(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
	}

	@Transactional
	public void createQuiz(QuizBean quizBean) {
		quizRepository.save(quizBean);
	}

	@Transactional(readOnly = true)
	public List<QuizBean> getAllQuizzes() {
		return quizRepository.findAll();
	}

	@Transactional(readOnly = true)
	public QuizBean getQuiz(int quizId) {
		return quizRepository.findById(quizId).orElseThrow(() -> new CustomException(QUIZ_NOT_FOUND));
	}

	@Transactional
	public void updateQuiz(int quizId, QuizBean quizBean) {
		QuizBean existingQuiz = getQuiz(quizId);
		existingQuiz.setQuizTitle(quizBean.getQuizTitle());
		existingQuiz.setQuizText(quizBean.getQuizText());
		quizRepository.save(existingQuiz);
	}

	@Transactional
	public void deleteQuiz(int quizId) {
		quizRepository.deleteById(quizId);
	}

	@Transactional
	public boolean submitAnswer(QuizBean quizBean) {
		QuizBean existingQuiz = getQuiz(quizBean.getQuizId());
		return existingQuiz.getQuizAnswer().equalsIgnoreCase(quizBean.getQuizAnswer());
	}

	// 강제 종료 메서드 추가
	@Transactional
	public void forcedEndQuiz(int roomId, String nickname) {
		// 퀴즈 강제 종료를 위한 로직 구현
		Map<String, Object> contentSet = new HashMap<>();
		contentSet.put("alert", nickname + " 님에 의해 게임이 강제로 종료되었습니다!");

		sendQuizMessage(roomId, QuizMessage.MessageType.FORCEDENDQUIZ, contentSet, null, nickname);

		// 필요한 정리 작업을 여기서 수행
		// 예를 들어, 퀴즈방 상태 변경, 관련된 엔티티 업데이트 등
	}

	// 퀴즈 메시지 전송 메서드
	public void sendQuizMessage(int roomId, QuizMessage.MessageType messageType, Map<String, Object> contentSet,
			String senderId, String sender) {
		QuizMessage<Object> quizMessage = new QuizMessage<>();
		quizMessage.setQuizRoomId(roomId);
		quizMessage.setSenderId(senderId);
		quizMessage.setSender(sender);
		quizMessage.setContent(contentSet);
		quizMessage.setType(messageType);

		// 실제 메시지 전송에 대한 로직을 추가 (예: WebSocket, 메시지 큐 등)
	}
}

package kr.co.duck.domain;

import org.springframework.stereotype.Service;

import kr.co.duck.repository.QuizRoomAttendeeRepository;
import kr.co.duck.repository.QuizRoomRepository;
import kr.co.duck.repository.QuizStartSetRepository;

@Service
public class QuizCommand {

	private final QuizRoomRepository quizRoomRepository;
	private final QuizRoomAttendeeRepository quizRoomAttendeeRepository;
	private final QuizStartSetRepository quizStartSetRepository;

	// 생성자
	public QuizCommand(QuizRoomRepository quizRoomRepository, QuizRoomAttendeeRepository quizRoomAttendeeRepository,
			QuizStartSetRepository quizStartSetRepository) {
		this.quizRoomRepository = quizRoomRepository;
		this.quizRoomAttendeeRepository = quizRoomAttendeeRepository;
		this.quizStartSetRepository = quizStartSetRepository;
	}

	////////////// TODO QuizRoom 관련
	// 퀴즈방 저장하기
	public void saveQuizRoom(QuizRoom quizRoom) {
		if (quizRoom.getQuizRoomType() == null || quizRoom.getQuizRoomType().isBlank()) {
			System.out.println("[ERROR] QuizRoom 엔티티에 quizRoomType이 설정되지 않았습니다.");
		} else {
			//System.out.println("[INFO] QuizRoom 저장: " + quizRoom);
		}
		quizRoomRepository.save(quizRoom);
	}

	// 퀴즈방 삭제하기
	public void deleteQuizRoom(QuizRoom quizRoom) {
		quizRoomRepository.delete(quizRoom);
	}

	////////////// TODO QuizRoomAttendee 관련
	// 참가자 저장
	public void saveQuizRoomAttendee(QuizRoomAttendee quizRoomAttendee) {
		quizRoomAttendeeRepository.save(quizRoomAttendee);
	}

	// 참가자 삭제
	public void deleteQuizRoomAttendee(QuizRoomAttendee quizRoomAttendee) {
		quizRoomAttendeeRepository.delete(quizRoomAttendee);
	}

	////////////// TODO QuizStartSet 관련
	// QuizStartSet 저장하기
	public void saveQuizStartSet(QuizStartSet quizStartSet) {
		quizStartSetRepository.save(quizStartSet);
	}

	// QuizStartSet 객체로 DB에서 삭제하기
	public void deleteQuizStartSetByRoomId(Long roomId) {
		quizStartSetRepository.deleteByRoomId(roomId);
	}


}

package kr.co.duck.repository;

import kr.co.duck.domain.QuizRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

// 기능: 퀴즈방 레포지토리
public interface QuizRoomRepository extends JpaRepository<QuizRoom, Long> {
	Page<QuizRoom> findAll(Pageable pageable); // 퀴즈방 전체 조회 페이징 처리

	Page<QuizRoom> findByQuizRoomNameContaining(Pageable pageable, String keyword); // 퀴즈방 페이징 처리 + 검색 기능

	Optional<QuizRoom> findByQuizRoomId(Long quizRoomId); // 퀴즈방 단건 조회

	@Lock(LockModeType.PESSIMISTIC_WRITE) // 동시 접속에 대한 충돌 방지용 비관적 락
	@Query("select q from QuizRoom q where q.quizRoomId = :quizRoomId")
	Optional<QuizRoom> findByQuizRoomId2(Long quizRoomId); // 퀴즈방 단건 조회 (락 적용)
}

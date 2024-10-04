package kr.co.duck.repository;

import kr.co.duck.domain.QuizStartSet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 기능: 퀴즈 시작 시 저장되는 스타트셋 레포지토리
public interface QuizStartSetRepository extends JpaRepository<QuizStartSet, Long> {
	Optional<QuizStartSet> findByRoomId(Long roomId); // 퀴즈룸 ID로 스타트 셋 찾기

	void deleteByRoomId(Long roomId); // 퀴즈룸 ID로 스타트 셋 지우기
}

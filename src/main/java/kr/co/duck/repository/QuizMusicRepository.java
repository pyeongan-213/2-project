package kr.co.duck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.QuizMusic;

@Repository
public interface QuizMusicRepository extends JpaRepository<QuizMusic, Integer> {
  
	//뮤직id로 퀴즈 검색
	List<QuizMusic> findByMusicId(int musicId);
	
	// 모든 퀴즈 검색
	List<QuizMusic> findAll();
}

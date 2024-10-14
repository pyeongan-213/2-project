package kr.co.duck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizMusicId;

@Repository
public interface QuizMusicRepository extends JpaRepository<QuizMusic, QuizMusicId> {

    // 특정 음악 ID로 퀴즈 검색
    List<QuizMusic> findByMusicId(int musicId);

    // 특정 퀴즈 ID에 해당하는 모든 음악 조회
    List<QuizMusic> findByQuizId(int quizId);

    // 모든 퀴즈 검색
    List<QuizMusic> findAll();
    
    // 특정 quizId와 musicId 조합이 존재하는지 확인하는 메서드
    boolean existsByQuizIdAndMusicId(int quizId, int musicId);
    

}

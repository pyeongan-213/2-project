package kr.co.duck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.duck.domain.QuizMusic;
import kr.co.duck.domain.QuizMusicId;

@Repository
public interface QuizMusicRepository extends JpaRepository<QuizMusic, QuizMusicId> {

    // 특정 음악 ID로 퀴즈 검색
    List<QuizMusic> findByMusicId(int musicId);

    // 특정 퀴즈 ID에 해당하는 모든 음악 조회
    List<QuizMusic> findByQuizId(int quizId);

    // 모든 퀴즈 음악 검색
    List<QuizMusic> findAll();

    // 특정 음악 ID가 이미 존재하는지 확인
    boolean existsByMusicId(int musicId);

    // 특정 quizId와 musicId 조합이 존재하는지 확인
    boolean existsByQuizIdAndMusicId(int quizId, int musicId);

    // **노래 제목을 공백 제거 및 소문자 변환 후 조회**
    @Query("SELECT qm FROM QuizMusic qm WHERE LOWER(REPLACE(qm.name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    List<QuizMusic> findByNormalizedSongTitle(@Param("name") String name);

    // **가수 이름을 공백 제거 및 소문자 변환 후 부분 일치 검색**
    @Query("SELECT qm FROM QuizMusic qm WHERE LOWER(REPLACE(qm.name, ' ', '')) LIKE CONCAT('%', LOWER(REPLACE(:artistName, ' ', '')), '%')")
    List<QuizMusic> findByNormalizedArtistName(@Param("artistName") String artistName);

    // **퀴즈 ID와 정답으로 음악 목록 조회 (공백 제거 및 대소문자 무시)**
    @Query("SELECT qm FROM QuizMusic qm WHERE qm.quizId = :quizId AND "
         + "LOWER(REPLACE(qm.name, ' ', '')) LIKE CONCAT('%', LOWER(REPLACE(:answer, ' ', '')), '%')")
    List<QuizMusic> findMusicByQuizIdAndAnswer(@Param("quizId") int quizId, @Param("answer") String answer);

    // **랜덤하게 N개의 퀴즈 음악을 조회하는 메서드 (Oracle DB)**
    @Query(value = "SELECT * FROM quiz_music ORDER BY DBMS_RANDOM.VALUE FETCH FIRST :maxSongs ROWS ONLY", 
           nativeQuery = true)
    List<QuizMusic> findRandomQuizzes(@Param("maxSongs") int maxSongs);
}

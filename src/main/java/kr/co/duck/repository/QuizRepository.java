package kr.co.duck.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kr.co.duck.beans.QuizBean;

@Repository
public interface QuizRepository extends JpaRepository<QuizBean, Integer> {
	// quizTitle에 특정 키워드가 포함된 퀴즈를 검색
	List<QuizBean> findByQuizTitleContaining(String keyword);

}

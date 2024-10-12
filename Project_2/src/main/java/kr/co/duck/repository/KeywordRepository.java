package kr.co.duck.repository;

import kr.co.duck.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

// 기능: 퀴즈 시작 시 주어지는 랜덤 키워드 레포지토리
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	// 랜덤 키워드 (4인일 경우)
	@Query(value = "SELECT * FROM keyword k WHERE k.category = :category ORDER BY RAND() LIMIT 4", nativeQuery = true)
	List<Keyword> findTop4ByCategory(@Param("category") String category);

	// 랜덤 키워드 (3인일 경우)
	@Query(value = "SELECT * FROM keyword k WHERE k.category = :category ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Keyword> findTop3ByCategory(@Param("category") String category);
}

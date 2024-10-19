package kr.co.duck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.duck.domain.MemberGameStats;

public interface MemberGameStatsRepository extends JpaRepository<MemberGameStats, Integer> {
    
    // 필요한 추가적인 메서드가 있으면 여기에 정의
	MemberGameStats findByMemberId(int memberId);
}

package kr.co.duck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.duck.domain.MemberGameStats;

public interface MemberGameStatsRepository extends JpaRepository<MemberGameStats, Integer> {
    
	MemberGameStats findByMemberId(int memberId);
	
	// 멤버 아이디로 스코어 찾기( 나중에 쿼리문 작성예정 )
	//MemberGameStats findByMemberscore(int memberId);

}



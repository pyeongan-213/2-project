package kr.co.duck.repository;

import kr.co.duck.domain.KakaopayInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface KaokaopayInfoRepository extends JpaRepository<KakaopayInfo,String> {

	// KakaopayInfo 엔티티의 sid 값 가져오기
    @Query("SELECT k.sid FROM KakaopayInfo k WHERE k.memberId = :member_id")
    String checkSubscribe(@Param("member_id") int member_id);
   
}
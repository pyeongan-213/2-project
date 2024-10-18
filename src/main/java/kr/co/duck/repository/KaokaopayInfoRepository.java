package kr.co.duck.repository;

import kr.co.duck.domain.KakaopayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KaokaopayInfoRepository extends JpaRepository<KakaopayInfo,String> {

   
   
   
   
}




/*
 * package kr.co.duck.repository;
 * 
 * import kr.co.duck.domain.KakaopayInfo; 
 * import kr.co.duck.domain.Payment;
 * import org.springframework.data.jpa.repository.JpaRepository; 
 * import org.springframework.stereotype.Repository;
 * 
 * 
 * public interface KaokaopayInfoRepository extends
 * JpaRepository<KakaopayInfo,String> {
 * 
 * 
 * @Repository public interface PaymentRepository extends JpaRepository<Payment,
 * Long> { Payment findByTid(String tid); }
 * 
 * }
 */
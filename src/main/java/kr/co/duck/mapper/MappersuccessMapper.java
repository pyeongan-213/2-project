package kr.co.duck.mapper;

import kr.co.duck.domain.Payment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MappersuccessMapper {

    /**
     * 결제 정보 저장
     * @param payment Payment 객체
     */
    @Insert("INSERT INTO PAY (PAY_ID, MEMBER_ID, SUBSCRIBEDATE, EXPIREDATE, CONTINUESUB, PRICE, PAYMENT, PAYMENT_INFO) " +
            "VALUES (#{payId}, #{memberId}, #{subscribeDate}, #{expireDate}, #{continueSub}, #{price}, #{payment}, #{paymentInfo})")
    void insertPayment(Payment payment);

    /**
     * 사용자 ID로 결제 내역 조회
     * @param memberId 사용자 ID
     * @return 결제 정보 리스트
     */
    @Select("SELECT * FROM PAY WHERE MEMBER_ID = #{memberId}")
    List<Payment> getPaymentsByMemberId(@Param("memberId") int memberId);

    /**
     * 결제 정보 업데이트
     * @param payment Payment 객체
     */
    @Update("UPDATE PAY SET PRICE = #{price}, PAYMENT = #{payment}, PAYMENT_INFO = #{paymentInfo} WHERE PAY_ID = #{payId}")
    void updatePayment(Payment payment);
}

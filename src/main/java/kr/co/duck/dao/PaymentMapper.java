package kr.co.duck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.duck.domain.Payment;

@Mapper
public interface PaymentMapper {

    // 결제 정보 삽입
    @Insert("INSERT INTO Payment (payment_id, member_id, subscription_date, expiry_date, is_renewed, payment_amount, payment_method, payment_info) " +
            "VALUES (#{payment_id}, #{member_id}, #{subscription_date}, #{expiry_date}, #{is_renewed}, #{payment_amount}, #{payment_method}, #{payment_info})")
    void insertPayment(Payment payment);

    // 특정 결제 정보 조회
    @Select("SELECT * FROM Payment WHERE payment_id = #{payment_id}")
    Payment getPayment(int payment_id);

    // 모든 결제 정보 조회
    @Select("SELECT * FROM Payment")
    List<Payment> getAllPayments();
    
    // 결제 정보 업데이트
    @Update("UPDATE Payment SET subscription_date = #{subscription_date}, expiry_date = #{expiry_date}, is_renewed = #{is_renewed}, " +
            "payment_amount = #{payment_amount}, payment_method = #{payment_method}, payment_info = #{payment_info} " +
            "WHERE payment_id = #{payment_id} AND member_id = #{member_id}")
    void updatePayment(Payment payment);

    // 결제 정보 삭제
    @Delete("DELETE FROM Payment WHERE payment_id = #{payment_id}")
    void deletePayment(int payment_id);
}


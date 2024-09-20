package kr.co.duck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.duck.dao.PaymentMapper;
import kr.co.duck.domain.Payment;

@Service
public class PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    // 결제 정보 삽입
    public void insertPayment(Payment payment) {
        paymentMapper.insertPayment(payment);
    }

    // 특정 결제 정보 조회
    public Payment getPayment(int paymentId) {
        return paymentMapper.getPayment(paymentId);
    }

    // 모든 결제 정보 조회
    public List<Payment> getAllPayments() {
        return paymentMapper.getAllPayments();
    }

    // 결제 정보 업데이트
    public void updatePayment(Payment payment) {
        paymentMapper.updatePayment(payment);
    }

    // 결제 정보 삭제
    public void deletePayment(int paymentId) {
        paymentMapper.deletePayment(paymentId);
    }
}

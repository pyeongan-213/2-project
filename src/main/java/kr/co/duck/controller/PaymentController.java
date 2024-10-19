package kr.co.duck.controller;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.domain.Payment;
import kr.co.duck.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Resource(name = "loginMemberBean")
   private MemberBean loginMemberBean;
    
    //               
    @PostMapping("/add")
    public String addPayment(@RequestBody Payment payment) {
        paymentService.insertPayment(payment);
        return "Payment added successfully!";
    }

    // Ư               ȸ
    @GetMapping("/{paymentId}")
    public Payment getPayment(@PathVariable int paymentId) {
        return paymentService.getPayment(paymentId);
    }

    //                 ȸ
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    //                 Ʈ
    @PutMapping("/update")
    public String updatePayment(@RequestBody Payment payment) {
        paymentService.updatePayment(payment);
        return "Payment updated successfully!";
    }

    //               
    @DeleteMapping("/delete/{paymentId}")
    public String deletePayment(@PathVariable int paymentId) {
        paymentService.deletePayment(paymentId);
        return "Payment deleted successfully!";
    }
}

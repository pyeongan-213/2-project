package kr.co.duck.controller;

import kr.co.duck.service.KakaoPayService;
import kr.co.duck.social.KaKaoApproveResponse;
import kr.co.duck.social.KaKaoReadyResponse;
import kr.co.duck.social.KakaoPayRedayDto;
import kr.co.duck.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/temp")
public class popup {

   @Autowired
   private KakaoPayService kakaoPayService;

	/*
	 * @GetMapping("/slide_popup") public String main() { return "temp/slide_popup";
	 * }
	 */
   
   
   @GetMapping("/slide_popup")
   public String main(Model model, @RequestParam(value = "success", required = false) Boolean success) {
       if (Boolean.TRUE.equals(success)) {
           model.addAttribute("successMessage", "결제 성공");
       }
       return "temp/slide_popup";
   }

   

   @PostMapping("/kakao/pay/ready")
   public @ResponseBody KaKaoReadyResponse kakaoPayReady(@RequestBody KakaoPayRedayDto dto){
      String name = dto.getName();
      String totalPrice = dto.getTotalPrice();

      KaKaoReadyResponse readyResponse = kakaoPayService.requestPaymentReady(name, totalPrice);
      SessionUtils.addAttribute("tid", readyResponse.getTid());

      return readyResponse;
   }

   @GetMapping("/kakao/pay/approve")
   public String kakaoPayApprove(@RequestParam("pg_token") String pgToken, Model model) {
      String tid = SessionUtils.getStringAttributeValue("tid");
      kakaoPayService.requestPaymentApprove(tid, pgToken);
      model.addAttribute("successMessage", "성공적으로 결제를 완료했습니다.");
      return "Project_2/temp/slide_popup";
   }


}
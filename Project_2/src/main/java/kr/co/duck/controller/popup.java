package kr.co.duck.controller;

import kr.co.duck.service.KakaoPayService;
import kr.co.duck.social.KaKaoApproveResponse;
import kr.co.duck.social.KaKaoReadyResponse;
import kr.co.duck.social.KakaoPayRedayDto;
import kr.co.duck.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/temp")
public class popup {

	private final KakaoPayService kakaoPayService;

	@GetMapping("/slide_popup")
	public String main() {
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
	public String kakaoPayApprove(@RequestParam("pg_token") String pgToken){
		String tid = SessionUtils.getStringAttributeValue("tid");
		KaKaoApproveResponse approveResponse =  kakaoPayService.requestPaymentApprove(tid, pgToken);
		return "redirect:/temp/slide_popup";
	}

}

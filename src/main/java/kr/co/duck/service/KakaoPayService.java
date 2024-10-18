package kr.co.duck.service;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.domain.KakaopayInfo;
import kr.co.duck.repository.KaokaopayInfoRepository;
import kr.co.duck.social.KaKaoApproveResponse;
import kr.co.duck.social.KaKaoReadyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private KaokaopayInfoRepository repository;
	
	@Autowired
	private HttpSession session; // HttpSession 주입
	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;

	private static final Logger log = LoggerFactory.getLogger(KakaoPayService.class);

	private final String READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
	private final String APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";

	@Value("${kakao.pay.key}")
	private String kakaoPayKey;


	public KaKaoReadyResponse requestPaymentReady(String name, String totalPrice) {
		try {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("cid", "TCSEQUENCE"); // 가맹점 코드(테스트용)
			parameters.put("partner_order_id", "1234567890"); // 주문번호
			parameters.put("partner_user_id", "testUser"); // 회원 아이디
			parameters.put("item_name", name); // 상품명
			parameters.put("quantity", "1"); // 상품 수량
			parameters.put("total_amount", String.valueOf(totalPrice)); // 상품 총액
			parameters.put("tax_free_amount", "0"); // 상품 비과세 금액
			parameters.put("approval_url", "http://localhost:8064/Project_2/temp/kakao/pay/approve"); // 결제 성공 시 URL
			parameters.put("cancel_url", "http://localhost:8064/Project_2/temp/kakao/pay/cancel"); // 결제 취소 시 URL
			parameters.put("fail_url", "http://localhost:8064/Project_2/temp/kakao/pay/fail"); // 결제 실패 시 URL

			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
			ResponseEntity<KaKaoReadyResponse> responseEntity = restTemplate.postForEntity(READY_URL, requestEntity,
					KaKaoReadyResponse.class);
			log.info("결제준비 응답객체: {}", responseEntity.getBody());

			return responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			log.error("카카오페이 결제 준비 중 오류 발생: {}", e.getMessage());
			throw e;
		}
	}

	// 결제 승인 메서드 수정
	public KaKaoApproveResponse requestPaymentApprove(String tid, String pgToken) {
		try {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("cid", "TCSEQUENCE"); // 가맹점 코드(테스트용)
			parameters.put("tid", tid); // 결제 고유번호
			parameters.put("partner_order_id", "1234567890"); // 주문번호
			parameters.put("partner_user_id", "testUser"); // 회원 아이디
			parameters.put("pg_token", pgToken); // 결제승인 요청을 인증 토큰

			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

			KaKaoApproveResponse approveResponse = restTemplate.postForObject(APPROVE_URL, requestEntity,
					KaKaoApproveResponse.class);
			log.info("결제승인 응답객체: {}", approveResponse);

			// HttpSession에서 memberID 가져오기
			MemberBean loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");
			if (loginMemberBean == null) {
				throw new IllegalStateException("로그인 정보가 없습니다.");
			}
			long memberID = (long) loginMemberBean.getMember_id(); // memberID 추출

			// memberID와 함께 KakaopayInfo 저장
			repository.save(KakaopayInfo.of(tid, Objects.requireNonNull(approveResponse).getSid(), memberID));

			return approveResponse;
		} catch (HttpClientErrorException e) {
			log.error("카카오페이 결제 승인 중 오류 발생: {}", e.getMessage());
			throw e;
		}
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "SECRET_KEY " + kakaoPayKey);
		headers.set("Content-type", "application/json");
		return headers;
	}
}

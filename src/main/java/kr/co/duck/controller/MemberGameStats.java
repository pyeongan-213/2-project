package kr.co.duck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import kr.co.duck.service.MemberService; // MemberService 임포트

@RestController
public class MemberGameStats {
	
	
	@Autowired
	private MemberService memberService; // MemberService 주입



	public MemberService getMemberService() {
		return memberService;
	}


	/*
	 * // 모든 리워드 조회
	 * 
	 * @GetMapping("/rewards") public ResponseEntity<RewardBean>
	 * allReward(HttpSession session) { // 세션에서 로그인된 사용자 정보 가져오기 MemberBean
	 * loginMemberBean = (MemberBean) session.getAttribute("loginMemberBean");
	 * 
	 * // 로그인 여부 확인 if (loginMemberBean == null || !loginMemberBean.isMemberLogin())
	 * { return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 응답 }
	 * 
	 * // MemberBean의 ID를 사용해 Member 엔티티 가져오기 Member member =
	 * memberService.findMemberById(loginMemberBean.getMember_id());
	 * 
	 * // Member가 없을 경우 처리 if (member == null) { return
	 * ResponseEntity.status(404).build(); // 404 Not Found 응답 }
	 * 
	 * //return ResponseUtil.response(allRewardList(member)); }
	 */
}

package kr.co.duck.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;
	
	@GetMapping("/join")
	public String join(@ModelAttribute("joinMemberBean") MemberBean joinMemberBean) {
		return "member/join";
	}
	
	@PostMapping("/join_pro")
	public String join_pro(@Valid @ModelAttribute("joinMemberBean") MemberBean joinMemberBean, BindingResult result) {
		
		if(result.hasErrors()) {
			return "member/join";
		}
		memberService.addMemberInfo(joinMemberBean);
		return "member/join_success";
	}
	
}

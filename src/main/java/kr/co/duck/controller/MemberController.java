package kr.co.duck.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.service.MemberService;
import kr.co.duck.validator.MemberValidator;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Resource(name = "loginMemberBean")
	private MemberBean loginMemberBean;

	@GetMapping("/login")
	public String login(@ModelAttribute("tempLoginMemberBean") MemberBean tempLoginMemberBean,
	                    @RequestParam(value = "fail", defaultValue = "false") boolean fail, 
	                    HttpServletRequest request, HttpSession session, Model model) {

	    model.addAttribute("fail", fail);

	    // 사용자가 로그인 페이지에 접근하기 전에 있었던 URL을 세션에 저장
	    String referer = request.getHeader("Referer");

	    // referer 값이 없거나, 로그인/회원 페이지에서 온 경우 기본적으로 메인 페이지로 리다이렉트
	    if (referer == null || referer.contains("/login") || referer.contains("/member")) {
	        referer = "/";
	    } else {
	        session.setAttribute("redirectURI", referer);  // 세션에 redirectURI 저장
	    }

	    //System.out.println("RedirectURI set to: " + referer);  // 로그로 출력

	    return "member/login";  // 로그인 페이지로 이동
	}


	@PostMapping("/login_pro")
	public String login_pro(@Valid @ModelAttribute("tempLoginMemberBean") MemberBean tempLoginMemberBean,
	                        BindingResult result, HttpSession session, HttpServletRequest request, Model model) {
	    if (result.hasErrors()) {
	        return "member/login";
	    }

	    // 사용자 인증 처리
	    memberService.getLoginMemberInfo(tempLoginMemberBean);

	    // 로그인 성공 여부 확인
	    if (loginMemberBean.isMemberLogin()) {
	        session.setAttribute("loginMemberBean", loginMemberBean);

	        // 세션에서 원래 페이지 URI 가져오기
	        String redirectURI = (String) session.getAttribute("redirectURI");

	        // 로그로 확인
	        //System.out.println("Redirect URI after login: " + redirectURI);

	        // redirectURI가 "/"가 아니라면 해당 경로로 리다이렉트
	        if (redirectURI != null && !redirectURI.equals("/") && !redirectURI.isEmpty()) {
	            session.removeAttribute("redirectURI");  // 한 번 사용 후 세션에서 제거
	            return "redirect:" + redirectURI;
	        }

	        // 리다이렉트 URI가 없을 경우 기본 페이지로 리다이렉트
	        return "redirect:/";
	    } else {
	        // 로그인 실패 시 처리
	    	model.addAttribute("fail", true);
	        return "member/login";
	    }
	}


	@GetMapping("/not_login")
	public String not_login() {
		return "member/not_login";
	}

	//Http 추가, 세션무효화 추가했어요
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        loginMemberBean.setMemberLogin(false);

        // 세션 무효화
        HttpSession session = request.getSession(false); // 기존 세션이 있으면 가져오고, 없으면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

		return "member/logout";
	}

	@GetMapping("/join")
	public String join(@ModelAttribute("joinMemberBean") MemberBean joinMemberBean) {
		return "member/join";
	}

	@PostMapping("/join_pro")
	public String join_pro(@Valid @ModelAttribute("joinMemberBean") MemberBean joinMemberBean, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "member/join";
		}
		memberService.addMemberInfo(joinMemberBean);
		
		// 회원가입 성공 플래그를 설정
	    model.addAttribute("joinSuccess", true);
		return "member/join";
	}

	@GetMapping("/info")
	public String info(@ModelAttribute("infoMemberBean") MemberBean infoMemberBean) {

		infoMemberBean = memberService.getModifyMemberInfo(infoMemberBean);

		return "member/info";
	}

	@PostMapping("/modify")
	public String modify(@ModelAttribute("modifyMemberBean") MemberBean modifyMemberBean) {

		modifyMemberBean = memberService.getModifyMemberInfo(modifyMemberBean);

		return "member/modify";
	}

	@PostMapping("/modify_pro")
	public String modify_pro(@Valid @ModelAttribute("modifyMemberBean") MemberBean modifyMemberBean,
	        BindingResult result, HttpSession session, Model model) {

	    if (result.hasErrors()) {
	        
	        return "member/modify";  // 다시 수정 페이지로 이동
	    }

	    // 수정 작업 수행
	    memberService.modifyMemberInfo(modifyMemberBean);

	    // 세션에 있는 로그인 된 회원 정보 갱신
	    loginMemberBean.setBirthday(modifyMemberBean.getBirthday());
	    loginMemberBean.setNickname(modifyMemberBean.getNickname());
	    loginMemberBean.setReal_name(modifyMemberBean.getReal_name());

	    // 세션에 갱신된 객체 설정
	    session.setAttribute("loginMemberBean", loginMemberBean);

	    // 성공 메시지를 모델에 추가
	    model.addAttribute("status", "success");
	    model.addAttribute("message", "정보가 수정되었습니다.");

	    return "member/modify";  // 수정 페이지로 다시 이동
	}


	@GetMapping("/delete_account")
	public String deleteAccount(@ModelAttribute("deleteMemberBean") MemberBean deleteMemberBean) {
		// 탈퇴 확인 화면을 렌더링 (비밀번호 입력 폼)
		return "member/delete_account";
	}

	@PostMapping("/delete_pro")
	public String deleteMember(@ModelAttribute("deleteMemberBean") MemberBean deleteMemberBean, Model model,
			HttpSession session) {

		// 입력한 비밀번호가 현재 로그인된 회원의 비밀번호와 일치하는지 확인
		boolean isPasswordCorrect = memberService.checkPassword(loginMemberBean.getMember_id(),
				deleteMemberBean.getPassword());

		if (!isPasswordCorrect) {
			// 비밀번호가 일치하지 않으면 탈퇴 실패
			model.addAttribute("fail", true);
			return "member/delete_account";
		}

		// 비밀번호가 일치하면 회원 탈퇴 처리
		memberService.deleteMemberAccount(loginMemberBean.getMember_id());

		// 세션 무효화 (로그아웃 처리)
		session.invalidate();

		// 성공 시 success 값을 true로 설정
		model.addAttribute("success", true);
		return "member/delete_account"; // 탈퇴 성공 화면으로 이동
	}

	
	@GetMapping("/modifyPassword")
	public String modifyPassword(@ModelAttribute("modifyPasswordBean") MemberBean modifyPasswordBean) {
		return "member/modifyPassword";
	}
	
	@PostMapping("/modifyPassword_pro")
	public String modifyPassword_pro(@Valid @ModelAttribute("modifyPasswordBean") MemberBean modifyPasswordBean, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "member/modifyPassword";
		}
		System.out.println(modifyPasswordBean.getPassword());
		memberService.modifyMemberPassword(modifyPasswordBean.getPassword(),modifyPasswordBean.getEmail());
		
		model.addAttribute("success", true);
		
		return "member/modifyPassword";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		MemberValidator validator1 = new MemberValidator();
		binder.addValidators(validator1);
	}

}

package kr.co.duck.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.duck.beans.MemberBean;

public class CheckLoginInterceptor implements HandlerInterceptor{

	private MemberBean loginMemberBean;
	
	public CheckLoginInterceptor(MemberBean loginMemberBean) {
		this.loginMemberBean = loginMemberBean;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {

	    if (!loginMemberBean.isMemberLogin()) {
	        String contextPath = request.getContextPath();
	        
	        // HTML 및 JavaScript 코드 작성
	        String script = "<script>" +
	                            "alert('로그인이 필요합니다. 로그인 화면으로 이동합니다.');" +
	                            "location.href='" + contextPath + "/member/login';" +
	                        "</script>";

	        // 응답 타입을 설정하고, JavaScript를 작성한 내용을 출력
	        response.setContentType("text/html; charset=UTF-8");
	        response.getWriter().write(script);
	        
	        return false;  // 컨트롤러로 요청이 넘어가지 않도록 false 반환
	    }
	    
	    return true;
	}

	
}

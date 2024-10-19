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

            // HTML 및 SweetAlert 스크립트를 작성
            String script = "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<meta charset='UTF-8'>" +
                            "<title>로그인 필요</title>" +
                            // SweetAlert CSS 및 JS CDN 추가
                            "<link href='https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css' rel='stylesheet'>" +
                            "<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js'></script>" +
                            "</head>" +
                            "<body>" +
                            "<script>" +
                            "Swal.fire({" +
                            "  title: '로그인이 필요합니다.'," +
                            "  text: '로그인 화면으로 이동합니다.'," +
                            "  icon: 'warning'," +
                            "  background: '#3A3A3A'," + // 사용자 요청에 따른 회색 배경색\r\n
                            "  color: '#fff'," + // 텍스트 색상 흰색
                            "  confirmButtonColor: '#1db954'," +  // 확인 버튼 색상 (Spotify 그린)
                            "  confirmButtonText: '확인'" +
                            "}).then((result) => {" +
                            "  if (result.isConfirmed) {" +
                            "    location.href='" + contextPath + "/member/login';" +  // 로그인 페이지로 리디렉션
                            "  }" +
                            "});" +
                            "</script>" +
                            "</body>" +
                            "</html>";

            // 응답 타입을 설정하고, JavaScript 및 HTML을 출력
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(script);

            return false;  // 컨트롤러로 요청이 넘어가지 않도록 false 반환
        }

        return true;
    }

	
}

package kr.co.duck.util;

import kr.co.duck.util.CustomException;
import kr.co.duck.util.GlobalResponseDto;
import kr.co.duck.util.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 기능 : Security에서 발생하는 예외를 별도 처리
public class SecurityExceptionFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            securityExceptionResponse(response, e.getStatusCode());
        } catch (IOException | ServletException e) {
            log.info("====================== SecurityExceptionFilter에서 처리한 에러 : {}", e.getMessage());
        }
    }

    // 예외 응답처리 메소드
    public void securityExceptionResponse(HttpServletResponse response, StatusCode statusCode) {
        response.setStatus(statusCode.getHttpStatus().value());
        response.setHeader("content-type", "application/json; charset=UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResponseDto(statusCode));
            response.getWriter().write(json);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.info("====================== SecurityExceptionFilter 예외 응답처리 예외 발생: {} ", e.getMessage());
        }
    }
}

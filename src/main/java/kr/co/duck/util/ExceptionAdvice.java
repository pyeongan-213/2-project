package kr.co.duck.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionAdvice {

	private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

	// 커스텀한 실행 예외
	@ExceptionHandler(value = { CustomException.class })
	protected ResponseEntity<?> handleCustomException(CustomException e) {
		log.error("====================== handleCustomException에서 처리한 에러 : {}", e.getMessage());
		return ResponseUtil.response(e.getStatusCode());
	}

	// 정규식 예외
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
		log.error("====================== handleValidationException에서 처리한 에러 : {}", e.getMessage());
		return ResponseUtil.response(StatusCode.INVALID_ID_PASSWORD);
	}
}

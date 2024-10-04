package kr.co.duck.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "kr.co.duck")
public class WebConfig implements WebMvcConfigurer {
	// 필요한 Web 설정을 추가할 수 있습니다.
}

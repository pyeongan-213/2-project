package kr.co.duck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/temp")
public class popup {

	
	@GetMapping("/slide_popup")
	public String main() {
		return "temp/slide_popup";

	}
}

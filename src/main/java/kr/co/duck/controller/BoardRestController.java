package kr.co.duck.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.duck.service.MailSendService;

@RestController
public class BoardRestController {

	@Autowired
	private MailSendService mailService;

	@PostMapping("/board/receiveEmail/{email}")
	public ResponseEntity<Void> receiveEmail(@PathVariable String email, @RequestParam String subject,
			@RequestParam String body) {
		mailService.receiveEmail(email, subject, body);

		String redirectUrl = ServletUriComponentsBuilder
							 .fromCurrentContextPath().path("/board/main")
							 .toUriString();

		return ResponseEntity
			   .status(HttpStatus.FOUND)
			   .location(URI.create(redirectUrl))
			   .build();
	}
}

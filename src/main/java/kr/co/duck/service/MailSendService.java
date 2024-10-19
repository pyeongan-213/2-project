package kr.co.duck.service;

import java.util.Random; 

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSendService {

	@Resource(name = "mailSender")
	private JavaMailSenderImpl mailSender;

	private int authNumber;

	public void makeRandomNumber() {
		Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111;
		System.out.println("인증번호 : " + checkNum);
		authNumber = checkNum;
	}

	public String modifyEmail(String email) {
		makeRandomNumber();
		String setFrom = "solduck0927@gmail.com";
		String toMail = email;
		String title = "Duck 회원정보 수정 인증번호";
		String content = "아래 인증코드를 입력해주세요." + "인증번호 : " + authNumber + "입니다.";
		mailSend(setFrom, toMail, title, content);
		return Integer.toString(authNumber);
	}

	public void mailSend(String setFrom, String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);

			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//게시판에서 문의하는 기능(해진 추가)
	public void receiveEmail(String name, String email, String subject, String body) {
		String from = email;
		String toMail = "haejin001228@naver.com";
		String title = subject;
		String content = "보낸 사람 : " + name + "(" + email + ")<br>" + "<hr>" + body.replace("\n", "<br>");
		mailReceive(from, toMail, title, content);
	}
	
	//게시판에서 문의하는 기능(해진 추가)
	public void mailReceive(String from, String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(from);
			helper.setTo(toMail);
			helper.setSubject(title);

			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
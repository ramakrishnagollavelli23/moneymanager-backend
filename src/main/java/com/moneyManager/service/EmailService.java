package com.moneyManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	
	@Value("ramakrishna.gollavelli10062003@gmail.com")
	private String fromEmail;

	public void sendEmail(String to,String subject,String body) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(fromEmail);
			simpleMailMessage.setTo(to);
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setText(body);
			javaMailSender.send(simpleMailMessage);
			
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}

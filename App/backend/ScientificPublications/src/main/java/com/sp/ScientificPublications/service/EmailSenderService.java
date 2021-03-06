package com.sp.ScientificPublications.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {
	
	@Autowired
    public JavaMailSender emailSender;
	
	//Might be useful for testing
	public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
	
	public void sendMessageWithAttachments(String to,
			String subject,
			String message,
			Map<String, ByteArrayResource> attachmentNameAndByteResource) {
		
		MimeMessage mimeMessage = emailSender.createMimeMessage();
        
        try {
        	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message);
			for (String attachmentName : attachmentNameAndByteResource.keySet()) {
				helper.addAttachment(attachmentName, attachmentNameAndByteResource.get(attachmentName));
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        
		emailSender.send(mimeMessage);
	}
	
	
}

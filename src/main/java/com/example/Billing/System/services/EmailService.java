package com.example.Billing.System.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMailWithReport(String toEmail, String subject, String body , InputStreamSource attachment , String filename) {
      try{
          MimeMessage message = javaMailSender.createMimeMessage();

          MimeMessageHelper mimeMessageHelper=  new MimeMessageHelper(message,true);
          mimeMessageHelper.setFrom(fromEmail);
          mimeMessageHelper.setTo(toEmail);
          mimeMessageHelper.setSubject(subject);
          mimeMessageHelper.setText(body);
          mimeMessageHelper.addAttachment(filename,attachment);

          javaMailSender.send(message);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }
}

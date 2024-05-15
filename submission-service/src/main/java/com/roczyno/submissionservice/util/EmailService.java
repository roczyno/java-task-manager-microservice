package com.roczyno.submissionservice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String body, String senderName) throws
            MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("adiabajacob9@gmail.com", senderName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }
}

package com.roczyno.notificationservice.service;

import com.roczyno.notificationservice.model.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;


	public  void sendTaskAssignedEmail(String receiverEmail, String username,
											 String taskId, String taskStatus, String taskDescription, EmailTemplate emailTemplate) throws MessagingException {

		String templateName=emailTemplate.name();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				mimeMessage,
				MULTIPART_MODE_MIXED,
				UTF_8.name()
		);
		Map<String, Object> properties = new HashMap<>();
		properties.put("username", username);
		properties.put("taskId", taskId);
		properties.put("taskStatus", taskStatus);
		properties.put("taskDescription", taskDescription);

		Context context = new Context();
		context.setVariables(properties);

		helper.setFrom("adiabajacob9@gmail.com");
		helper.setTo(receiverEmail);
		helper.setSubject("Task Assigned");

		String template = templateEngine.process(templateName, context);

		helper.setText(template, true);

		mailSender.send(mimeMessage);



	}
}

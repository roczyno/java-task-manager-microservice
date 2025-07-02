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

	public void sendTaskSubmittedEmail(
			String assigneeUsername,
			String assignerUsername,
			String taskId,
			String taskTitle,
			String taskDescription,
			String githubLink,
			String deployedUrl
	) throws MessagingException {
		String templateName = EmailTemplate.TASK_SUBMITTED.name();

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				mimeMessage,
				MULTIPART_MODE_MIXED,
				UTF_8.name()
		);


		Map<String, Object> properties = new HashMap<>();
		properties.put("assigneeUsername", assigneeUsername);
		properties.put("assignerUsername", assignerUsername);
		properties.put("taskId", taskId);
		properties.put("taskTitle", taskTitle);
		properties.put("taskDescription", taskDescription);
		properties.put("githubLink", githubLink);
		properties.put("deployedUrl", deployedUrl);

		Context context = new Context();
		context.setVariables(properties);


		helper.setFrom("adiabajacob9@gmail.com");
		helper.setTo(assignerUsername);
		helper.setSubject("Task Submitted: " + taskTitle);

		String template = templateEngine.process(templateName, context);

		helper.setText(template, true);


		mailSender.send(mimeMessage);
	}

	public void sendSubmissionAcceptedEmail(String assignedUsername) throws MessagingException {
		String templateName = EmailTemplate.SUBMISSION_ACCEPTED.name();

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				mimeMessage,
				MULTIPART_MODE_MIXED,
				UTF_8.name()
		);

		Map<String, Object> properties = new HashMap<>();
		properties.put("username", assignedUsername);

		Context context = new Context();
		context.setVariables(properties);

		helper.setFrom("adiabajacob9@gmail.com");
		helper.setTo(assignedUsername);
		helper.setSubject("Your Task Submission was Accepted");

		String template = templateEngine.process(templateName, context);
		helper.setText(template, true);

		mailSender.send(mimeMessage);
	}

	public void sendSubmissionDeclinedEmail(String assignedUsername) throws MessagingException {
		String templateName = EmailTemplate.SUBMISSION_DECLINED.name();

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				mimeMessage,
				MULTIPART_MODE_MIXED,
				UTF_8.name()
		);

		Map<String, Object> properties = new HashMap<>();
		properties.put("username", assignedUsername);

		Context context = new Context();
		context.setVariables(properties);

		helper.setFrom("adiabajacob9@gmail.com");
		helper.setTo(assignedUsername);
		helper.setSubject("Your Task Submission was Declined");

		String template = templateEngine.process(templateName, context);
		helper.setText(template, true);

		mailSender.send(mimeMessage);
	}

}

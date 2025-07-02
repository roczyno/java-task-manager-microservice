package com.roczyno.submissionservice.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubmissionProducer {
	private final RabbitTemplate rabbitTemplate;


	public void sendSubmissionMessage(String assigneeUsername, Long id, String title, String description, String assignedUsername,
									  String githubLink, String deployedUrl, String string) {
		TaskSubmitted taskSubmitted=new TaskSubmitted(assigneeUsername,assignedUsername,id.toString(),title,description,githubLink,deployedUrl);
		rabbitTemplate.convertAndSend("task-submitted", taskSubmitted);
	}

	public void sendAcceptedMessage(String assignedUser) {
		TaskAccepted taskAccepted=new TaskAccepted(assignedUser);
		rabbitTemplate.convertAndSend("submission-accepted", taskAccepted);
	}

	public void sendDeclinedMessage(String assignedUser) {
		TaskDeclined taskDeclined=new TaskDeclined(assignedUser);
		rabbitTemplate.convertAndSend("submission-declined", taskDeclined);
	}
}

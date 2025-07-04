package com.roczyno.notificationservice.rabbitmq.submissionservice;

import com.roczyno.notificationservice.model.Notification;
import com.roczyno.notificationservice.model.NotificationType;
import com.roczyno.notificationservice.service.EmailService;
import com.roczyno.notificationservice.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SubmissionConsumer {

	private final NotificationService notificationService;
	private final EmailService emailService;

	@RabbitListener(queues = "task-submitted")
	public void consumeMessage(TaskSubmitted taskSubmitted) {
		Notification notification= Notification.builder()
				.notificationDate(LocalDateTime.now())
				.notificationType(NotificationType.TASK_ASSIGNED)
				.taskSubmitted(taskSubmitted)
				.build();

		notificationService.saveNotification(notification);
		try {
			emailService.sendTaskSubmittedEmail(taskSubmitted.assignedUsername(),taskSubmitted.assigneeUsername(),
					taskSubmitted.taskId(),taskSubmitted.taskTitle(),taskSubmitted.taskDescription(),
					taskSubmitted.githubLink(),taskSubmitted.deployedUrl());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}


	}

	@RabbitListener(queues = "submission-accepted")
	public void consumeMessage(TaskAccepted taskAccepted) {
		Notification notification=Notification.builder()
				.notificationDate(LocalDateTime.now())
				.notificationType(NotificationType.TASK_ACCEPTED)
				.taskAccepted(taskAccepted)
				.build();
		notificationService.saveNotification(notification);

		try {
			emailService.sendSubmissionAcceptedEmail(taskAccepted.assignedUsername());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@RabbitListener(queues = "submission-declined")
	public void consumeMessage(TaskDeclined taskDeclined) {
		Notification notification=Notification.builder()
				.notificationDate(LocalDateTime.now())
				.notificationType(NotificationType.TASK_REJECTED)
				.taskDeclined(taskDeclined)
				.build();
		notificationService.saveNotification(notification);

		try {
			emailService.sendSubmissionDeclinedEmail(taskDeclined.assignedUsername());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}

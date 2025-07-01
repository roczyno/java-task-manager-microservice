package com.roczyno.notificationservice.rabbitmq;

import com.roczyno.notificationservice.model.EmailTemplate;
import com.roczyno.notificationservice.model.Notification;
import com.roczyno.notificationservice.model.NotificationType;
import com.roczyno.notificationservice.service.EmailService;
import com.roczyno.notificationservice.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class TaskConsumer {

	private final NotificationService notificationService;
	private final EmailService emailService;

	@RabbitListener(queues = "task-assigned")
	public void consumeMessage(TaskAssigned taskAssigned) {
		Notification notification= Notification.builder()
				.notificationDate(LocalDateTime.now())
				.notificationType(NotificationType.TASK_ASSIGNED)
				.taskAssigned(taskAssigned)
				.build();

		notificationService.saveNotification(notification);
		try {
			emailService.sendTaskAssignedEmail(taskAssigned.email(),taskAssigned.username(),taskAssigned.taskId(),
					taskAssigned.taskStatus(),taskAssigned.taskDescription(), EmailTemplate.TASK_ASSIGNED);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}


	}


}

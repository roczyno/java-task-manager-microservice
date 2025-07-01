package com.roczyno.notificationservice.model;

import com.roczyno.notificationservice.rabbitmq.submissionservice.TaskSubmitted;
import com.roczyno.notificationservice.rabbitmq.taskservice.TaskAssigned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Notification {
	@Id
	private String id;
	private NotificationType notificationType;
	private LocalDateTime notificationDate;
	private TaskAssigned taskAssigned;
	private TaskSubmitted taskSubmitted;
}

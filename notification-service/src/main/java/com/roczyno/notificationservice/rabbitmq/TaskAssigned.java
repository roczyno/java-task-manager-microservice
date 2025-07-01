package com.roczyno.notificationservice.rabbitmq;


public record TaskAssigned(
		String email,
		String username,
		String taskId,
		String taskTitle,
		String taskDescription,
		String taskStatus

) {
}

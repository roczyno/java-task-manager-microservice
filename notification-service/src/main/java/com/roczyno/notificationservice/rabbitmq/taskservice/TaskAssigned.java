package com.roczyno.notificationservice.rabbitmq.taskservice;


public record TaskAssigned(
		String email,
		String username,
		String taskId,
		String taskTitle,
		String taskDescription,
		String taskStatus

) {
}

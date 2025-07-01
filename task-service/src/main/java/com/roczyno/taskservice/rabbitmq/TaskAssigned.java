package com.roczyno.taskservice.rabbitmq;


public record TaskAssigned(
		String email,
		String username,
		String taskId,
		String taskTitle,
		String taskDescription,
		String taskStatus

) {
}

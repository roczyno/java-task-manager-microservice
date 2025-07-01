package com.roczyno.notificationservice.rabbitmq.submissionservice;


public record TaskSubmitted(
		String assigneeUsername,
		String assignedUsername,
		String taskId,
		String taskTitle,
		String taskDescription,
		String githubLink,
		String deployedUrl


) {
}

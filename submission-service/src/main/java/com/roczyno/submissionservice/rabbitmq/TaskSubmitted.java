package com.roczyno.submissionservice.rabbitmq;


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

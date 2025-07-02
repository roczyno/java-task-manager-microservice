package com.roczyno.notificationservice.rabbitmq.submissionservice;

public record TaskDeclined(
		String assignedUsername
) {
}

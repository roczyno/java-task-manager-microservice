package com.roczyno.submissionservice.rabbitmq;

public record TaskDeclined(
		String assignedUsername
) {
}

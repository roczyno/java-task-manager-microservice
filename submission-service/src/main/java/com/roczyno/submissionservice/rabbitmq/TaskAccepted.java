package com.roczyno.submissionservice.rabbitmq;

public record TaskAccepted(
		String assignedUsername
) {
}

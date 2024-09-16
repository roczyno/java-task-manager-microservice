package com.roczyno.taskservice.request;

import com.roczyno.taskservice.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskRequest(
		String title,
		String description,
		String image,
		List<String> tags,
		LocalDateTime deadline
) {
}

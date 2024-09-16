package com.roczyno.taskservice.response;

import com.roczyno.taskservice.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
		Long id,
		String title,
		String description,
		TaskStatus status,
		Long assignedUserId,
		Long assigneeUserId,
		String image,
		List<String> tags,
		LocalDateTime deadline
) {
}

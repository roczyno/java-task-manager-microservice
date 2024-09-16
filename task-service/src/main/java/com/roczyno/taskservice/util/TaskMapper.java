package com.roczyno.taskservice.util;

import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.response.TaskResponse;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
	public TaskResponse mapToTaskResponse(Task task) {
		return new TaskResponse(
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				task.getStatus(),
				task.getAssignedUserId(),
				task.getAssigneeUserId(),
				task.getImage(),
				task.getTags(),
				task.getDeadline()
		);
	}
}

package com.roczyno.taskservice.rabbitmq;

import com.roczyno.taskservice.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskProducer {
	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(Task task,String username,String email) {
		TaskAssigned taskAssigned =
				new TaskAssigned(email,username, task.getId().toString(),
						task.getTitle(), task.getDescription(), task.getStatus().toString());

		rabbitTemplate.convertAndSend("task-assigned", taskAssigned);
	}
}

package com.roczyno.taskservice.service;

import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.request.TaskRequest;
import com.roczyno.taskservice.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest task, String jwt);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllTasks(TaskStatus taskStatus);
    TaskResponse updateTask(TaskRequest task,Long id,String jwt);
    String deleteTask(Long id, String jwt);
    TaskResponse assignedToUser(Long userId, Long taskId,String jwt);
    List<TaskResponse> getAssignedUsersTasks(Long userId,TaskStatus taskStatus);
    TaskResponse completeTask(Long id);
}

package com.roczyno.taskservice.service;

import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;

import java.util.List;

public interface TaskService {
    Task createTask(Task task,String requesterRole) throws Exception;
    Task getTaskById(Long id) throws Exception;
    List<Task> getAllTasks(TaskStatus taskStatus) throws Exception;
    Task updateTask(Task task,Long id,Long userId) throws Exception;
    void deleteTask(Long id) throws Exception;
    Task assignedToUser(Long userId, Long taskId,String jwt) throws Exception;
    List<Task> getAssignedUsersTasks(Long userId,TaskStatus taskStatus) throws Exception;
    Task completeTask(Long id) throws Exception;
}

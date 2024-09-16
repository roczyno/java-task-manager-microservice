package com.roczyno.taskservice.service;

import com.roczyno.taskservice.exception.TaskException;
import com.roczyno.taskservice.external.UserDto;
import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.repository.TaskRepository;
import com.roczyno.taskservice.request.TaskRequest;
import com.roczyno.taskservice.response.TaskResponse;
import com.roczyno.taskservice.util.EmailService;
import com.roczyno.taskservice.util.TaskMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final TaskMapper taskMapper;


    @Override
    public TaskResponse createTask(TaskRequest request, String jwt)  {
        String imageUrl = "https://source.unsplash.com/random?code";
        UserDto user=userService.getUserProfile(jwt);

        if (!user.getRole().equals(("ADMIN"))) {
            throw new TaskException("Only ADMIN roles are allowed to create tasks");
        }

        Task task=new Task();
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        task.setAssigneeUserId(user.getId());
        task.setImage(imageUrl);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDeadline(request.deadline());
        task.setTags(request.tags());
       Task savedTask= taskRepository.save(task);
       return taskMapper.mapToTaskResponse(savedTask);
    }

    @Override
    public TaskResponse getTaskById(Long id)  {
       Task task= taskRepository.findById(id)
                .orElseThrow(()-> new TaskException("Task not found"));
       return taskMapper.mapToTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks(TaskStatus taskStatus)  {
        List<Task> allTasks = taskRepository.findAll();
        return allTasks.stream()
                .filter(task -> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .map(taskMapper::mapToTaskResponse)
                .toList();
    }


    @Override
    public TaskResponse updateTask(TaskRequest task, Long id, String jwt)  {
        UserDto user=userService.getUserProfile(jwt);
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(()-> new TaskException("Task not found"));
        verifyOwnership(user,taskToUpdate);

        if (task.deadline() != null) {
            taskToUpdate.setDeadline(task.deadline());
        }
        if (task.image() != null) {
            taskToUpdate.setImage(task.image());
        }
        if (task.title() != null) {
            taskToUpdate.setTitle(task.title());
        }
        if (task.description() != null) {
            taskToUpdate.setDescription(task.description());
        }
        if (task.description() != null) {
            taskToUpdate.setDescription(task.description());
        }
        Task updatedTask= taskRepository.save(taskToUpdate);
        return taskMapper.mapToTaskResponse(updatedTask);
    }

    @Override
    public String deleteTask(Long id, String jwt)  {
        UserDto user=userService.getUserProfile(jwt);
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(()-> new TaskException("Task not found"));
        verifyOwnership(user,taskToDelete);
        taskRepository.delete(taskToDelete);
      return "task deleted successfully";
    }

    private void verifyOwnership(UserDto user, Task task) {
        if(!user.getId().equals(task.getAssigneeUserId())){
            throw new TaskException("You can not modify");
        }
    }

    @Override
    public TaskResponse assignedToUser(Long userId, Long taskId, String jwt)  {
        Task taskToAssign = taskRepository.findById(taskId)
                .orElseThrow(()-> new TaskException("Task not found"));
        UserDto user = userService.getUserById(userId, jwt);
        if (taskToAssign == null) {
            throw new TaskException("Task not found");
        }
        taskToAssign.setAssignedUserId(userId);
        taskToAssign.setStatus(TaskStatus.ASSIGNED);

        String emailBody = String.format(
                "<html>" +
                        "<body>" +
                        "<h2>Hello %s,</h2>" +
                        "<p>You have been assigned a new task. Below are the details:</p>" +
                        "<table border='1' style='border-collapse: collapse; width: 100%%;'>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task ID</th><td style='padding: 8px;'>%d</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task Name</th><td style='padding: 8px;'>%s</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task Description</th><td style='padding: 8px;'>%s</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task Deadline</th><td style='padding: 8px;'>%s</td></tr>"+
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Status</th><td style='padding: 8px;'>%s</td></tr>" +
                        "</table>" +
                        "<p>Please complete this task as soon as possible.</p>" +
                        "<p>Please login to <a href=\"http://localhost:5173\">our website</a> to find more details.</p>" +
                        "<p>Best regards,<br>ADMIN</p>" +
                        "</body>" +
                        "</html>",
                user.getUsername(),
                taskToAssign.getId(),
                taskToAssign.getTitle(),
                taskToAssign.getDescription(),
                taskToAssign.getDeadline(),
                taskToAssign.getStatus().name()
        );

		try {
			emailService.sendSimpleMessage(user.getEmail(), "New Task Assigned to You",
                    emailBody, "Task Manager");
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		Task assignedTask= taskRepository.save(taskToAssign);
        return taskMapper.mapToTaskResponse(assignedTask);
    }


    @Override
    public List<TaskResponse> getAssignedUsersTasks(Long userId, TaskStatus taskStatus)  {
        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        return tasks.stream()
                .filter(task -> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .map(taskMapper::mapToTaskResponse)
                .toList();

    }

    @Override
    public TaskResponse completeTask(Long id)  {
        Task taskToComplete = taskRepository.findById(id)
                .orElseThrow(()-> new TaskException("Task not found"));
        if (taskToComplete == null) {
            throw new TaskException("Task not found");
        }
        taskToComplete.setStatus(TaskStatus.DONE);
       Task completedTask= taskRepository.save(taskToComplete);
       return taskMapper.mapToTaskResponse(completedTask);

    }
}

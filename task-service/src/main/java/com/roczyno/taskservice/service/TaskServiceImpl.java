package com.roczyno.taskservice.service;

import com.roczyno.taskservice.external.UserDto;
import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.repository.TaskRepository;
import com.roczyno.taskservice.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final UserService userService;


    @Override
    public Task createTask(Task task, String requesterRole,Long userId) throws Exception {
        String imageUrl = "https://source.unsplash.com/random?code";

        if (!requesterRole.equals(("ADMIN"))) {
            throw new Exception("Only ADMIN roles are allowed to create tasks");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        task.setAssigneeUserId(userId);
        task.setImage(imageUrl);
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws Exception {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<Task> getAllTasks(TaskStatus taskStatus) throws Exception {
        List<Task> allTasks = taskRepository.findAll();
        return allTasks.stream()
                .filter(task -> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .collect(Collectors.toList());
    }


    @Override
    public Task updateTask(Task task, Long id, Long userId) throws Exception {
        Task taskToUpdate = getTaskById(id);
        if (taskToUpdate == null) {
            throw new Exception("Task not found");
        }
        if (task.getDeadline() != null) {
            taskToUpdate.setDeadline(task.getDeadline());
        }
        if (task.getImage() != null) {
            taskToUpdate.setImage(task.getImage());
        }
        if (task.getTitle() != null) {
            taskToUpdate.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            taskToUpdate.setDescription(task.getDescription());
        }
        if (task.getDescription() != null) {
            taskToUpdate.setDescription(task.getDescription());
        }
        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Long id) throws Exception {
        Task taskToDelete = getTaskById(id);
        if (taskToDelete == null) {
            throw new Exception("Task not found");
        }
        taskRepository.delete(taskToDelete);

    }

    @Override
    public Task assignedToUser(Long userId, Long taskId, String jwt) throws Exception {
        Task taskToAssign = getTaskById(taskId);
        UserDto user = userService.getUserById(userId, jwt);
        if (taskToAssign == null) {
            throw new Exception("Task not found");
        }
        taskToAssign.setAssignedUserId(userId);
        taskToAssign.setStatus(TaskStatus.ASSIGNED);

        // Create a detailed message body
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

        emailService.sendSimpleMessage(user.getEmail(), "New Task Assigned to You", emailBody, "Task Manager");

        return taskRepository.save(taskToAssign);
    }


    @Override
    public List<Task> getAssignedUsersTasks(Long userId, TaskStatus taskStatus) throws Exception {
        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        return tasks.stream()
                .filter(task -> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .collect(Collectors.toList());

    }

    @Override
    public Task completeTask(Long id) throws Exception {
        Task taskToComplete = getTaskById(id);
        if (taskToComplete == null) {
            throw new Exception("Task not found");
        }
        taskToComplete.setStatus(TaskStatus.DONE);
        return taskRepository.save(taskToComplete);

    }
}

package com.roczyno.taskservice.service;

import com.roczyno.taskservice.exception.TaskException;
import com.roczyno.taskservice.external.Role;
import com.roczyno.taskservice.external.UserDto;
import com.roczyno.taskservice.external.UserService;
import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.rabbitmq.TaskProducer;
import com.roczyno.taskservice.repository.TaskRepository;
import com.roczyno.taskservice.request.TaskRequest;
import com.roczyno.taskservice.response.TaskResponse;
import com.roczyno.taskservice.util.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final TaskProducer taskProducer;
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);



    @Override
    public TaskResponse createTask(TaskRequest request, String jwt)  {
        log.info("start to create task");
        String imageUrl = "https://source.unsplash.com/random?code";
        UserDto user=userService.getUserProfile(jwt);

        if (!user.getRole().equals((Role.ADMIN))) {
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
        log.info("task created");
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


		Task assignedTask= taskRepository.save(taskToAssign);
        taskProducer.sendMessage(assignedTask,user.getUsername(),user.getEmail());

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

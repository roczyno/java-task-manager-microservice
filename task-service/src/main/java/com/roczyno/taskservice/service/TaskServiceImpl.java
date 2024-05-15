package com.roczyno.taskservice.service;

import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task, String requesterRole) throws Exception {
        if(!requesterRole.equals(("ADMIN"))){
            throw new Exception("Only ADMIN roles are allowed to create tasks");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
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
        if(taskToUpdate == null){
            throw new Exception("Task not found");
        }
        if(task.getDeadline()!=null){
            taskToUpdate.setDeadline(task.getDeadline());
        }
        if(task.getImage()!=null){
            taskToUpdate.setImage(task.getImage());
        }
        if(task.getTitle()!=null){
            taskToUpdate.setTitle(task.getTitle());
        }
        if(task.getDescription()!=null){
            taskToUpdate.setDescription(task.getDescription());
        }
        if(task.getDescription()!=null){
            taskToUpdate.setDescription(task.getDescription());
        }
        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Long id) throws Exception {
        Task taskToDelete = getTaskById(id);
        if(taskToDelete == null){
            throw new Exception("Task not found");
        }
        taskRepository.delete(taskToDelete);

    }

    @Override
    public Task assignedToUser(Long userId, Long taskId) throws Exception {
        Task taskToAssign = getTaskById(taskId);
        if(taskToAssign == null){
            throw new Exception("Task not found");
        }
        taskToAssign.setAssignedUserId(userId);
        taskToAssign.setStatus(TaskStatus.DONE);
        return taskRepository.save(taskToAssign);
    }

    @Override
    public List<Task> getAssignedUsersTasks(Long userId, TaskStatus taskStatus) throws Exception {
       List<Task> tasks=taskRepository.findByAssignedUserId(userId);
        return tasks.stream()
                .filter(task -> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .collect(Collectors.toList());

    }

    @Override
    public Task completeTask(Long id) throws Exception {
        Task taskToComplete = getTaskById(id);
        if(taskToComplete == null){
            throw new Exception("Task not found");
        }
        taskToComplete.setStatus(TaskStatus.DONE);
        return taskRepository.save(taskToComplete);

    }
}

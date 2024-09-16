package com.roczyno.taskservice.controller;

import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.request.TaskRequest;
import com.roczyno.taskservice.response.TaskResponse;
import com.roczyno.taskservice.service.TaskService;
import com.roczyno.taskservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest task, @RequestHeader("Authorization") String jwt)  {
        return ResponseEntity.ok( taskService.createTask(task,jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id)  {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping("/assign/{taskId}/user/{userId}")
    public ResponseEntity<TaskResponse> assignedTaskToUser( @RequestHeader("Authorization") String jwt,@PathVariable Long taskId,
                                                    @PathVariable Long userId)  {
       return ResponseEntity.ok(taskService.assignedToUser(userId,taskId,jwt));

    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskResponse>> getUserTasks(@RequestHeader("Authorization") String jwt,
                                                   @RequestParam(required = false)TaskStatus taskStatus)  {
        return ResponseEntity.ok(taskService.getAssignedUsersTasks(userService.getUserProfile(jwt).getId(),taskStatus));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks(@RequestParam(required = false)TaskStatus taskStatus)  {

        return ResponseEntity.ok(taskService.getAllTasks(taskStatus));

    }

    @PutMapping("/{id}/update")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskRequest task,
                                           @RequestHeader("Authorization") String jwt)  {
        return ResponseEntity.ok(taskService.updateTask(task,id,jwt));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt)  {
        return ResponseEntity.ok(taskService.deleteTask(id,jwt));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
       return ResponseEntity.ok(taskService.completeTask(id));
    }


}

package com.roczyno.taskservice.controller;

import com.roczyno.taskservice.external.UserDto;
import com.roczyno.taskservice.model.Task;
import com.roczyno.taskservice.model.TaskStatus;
import com.roczyno.taskservice.service.TaskService;
import com.roczyno.taskservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user=userService.getUserProfile(jwt);
        String role= user.getRole();
        Task res=taskService.createTask(task,role);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        userService.getUserProfile(jwt);
        Task task= taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/assign/{taskId}/user/{userId}")
    public ResponseEntity<Task> assignedTaskToUser( @RequestHeader("Authorization") String jwt,@PathVariable Long taskId,
                                                    @PathVariable Long userId) throws Exception {

       Task task= taskService.assignedToUser(userId,taskId,jwt);
       return ResponseEntity.ok(task);

    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> getUserTasks(@RequestHeader("Authorization") String jwt,
                                                   @RequestParam(required = false)TaskStatus taskStatus) throws Exception {
        UserDto user=userService.getUserProfile(jwt);
        List<Task> tasks= taskService.getAssignedUsersTasks(user.getId(),taskStatus);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(@RequestHeader("Authorization") String jwt,
                                                  @RequestParam(required = false)TaskStatus taskStatus) throws Exception {
        userService.getUserProfile(jwt);
        List<Task> tasks= taskService.getAllTasks(taskStatus);
        return ResponseEntity.ok(tasks);

    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody Task task,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user=userService.getUserProfile(jwt);
        Task updatedTask= taskService.updateTask(task,id,user.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        userService.getUserProfile(jwt);
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) throws Exception{
        userService.getUserProfile(jwt);
       Task task= taskService.completeTask(id);
       return ResponseEntity.ok(task);
    }


}

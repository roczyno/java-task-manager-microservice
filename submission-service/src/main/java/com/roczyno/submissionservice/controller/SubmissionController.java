package com.roczyno.submissionservice.controller;

import com.roczyno.submissionservice.external.task.TaskService;
import com.roczyno.submissionservice.external.user.User;
import com.roczyno.submissionservice.external.user.UserService;
import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final UserService userService;
    private final TaskService  taskService;


    public SubmissionController(SubmissionService submissionService, UserService userService, TaskService taskService) {
        this.submissionService = submissionService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<Submission> submitTask(@RequestParam Long taskId, @RequestParam String githubLink,@RequestParam String deployedUrl,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.getUserProfile(jwt);
        Submission sub=submissionService.submitTask(taskId,user.getId(),githubLink,jwt,deployedUrl);
        return ResponseEntity.ok(sub);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable("id") Long id, @RequestParam String githubLink,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
     userService.getUserProfile(jwt);
        Submission sub=submissionService.getTaskSubmission(id);
        return ResponseEntity.ok(sub);

    }
    @GetMapping("/all")
    public ResponseEntity<List<Submission>> getAllSubmissions(@RequestHeader("Authorization") String jwt){
        userService.getUserProfile(jwt);
        List<Submission> sub=submissionService.getTaskSubmissions();
        return ResponseEntity.ok(sub);
    }

    @GetMapping("/all/task/{id}")
    public ResponseEntity<List<Submission>> getAllSubmissionsOfTask(@PathVariable Long id,@RequestHeader("Authorization") String jwt){
        userService.getUserProfile(jwt);
        List<Submission> sub=submissionService.getTaskSubmissionsByTaskId(id);
        return ResponseEntity.ok(sub);
    }

//public ResponseEntity<Submission> updateSubmission(@RequestHeader("Authorization") String jwt,Submission submission){
//    List<Submission> sub=submissionService.updateTaskSubmission()
//    return ResponseEntity.ok(sub);
//}
@PutMapping("/{id}")
public ResponseEntity<Submission> acceptOrDeclineSubmission(@RequestHeader("Authorization") String jwt,@PathVariable Long id,
                                                            @RequestParam String status) throws Exception {
    Submission sub=submissionService.acceptDecline(id,status,jwt);
    return ResponseEntity.ok(sub);
}

}

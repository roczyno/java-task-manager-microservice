package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.external.task.TaskDto;
import com.roczyno.submissionservice.external.task.TaskService;
import com.roczyno.submissionservice.external.user.User;
import com.roczyno.submissionservice.external.user.UserService;
import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.repository.SubmissionRepository;
import com.roczyno.submissionservice.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl  implements SubmissionService{
    private final SubmissionRepository submissionRepository;
    private final TaskService taskService;
    private final EmailService emailService;
    private final UserService userService;

    @Override
    public Submission submitTask(Long taskId, Long userId, String githubLink, String jwt, String deployedUrl) throws Exception {
        TaskDto task = taskService.getTask(taskId, jwt);
        User assigneeUser = userService.getUserById(task.getAssigneeUserId(), jwt);
        User assignedUser = userService.getUserById(userId, jwt);
        Submission sub = new Submission();
        sub.setTaskId(task.getId());
        sub.setUserId(userId);
        sub.setGithubLink(githubLink);
        sub.setDeployedUrl(deployedUrl);
        sub.setSubmissionTime(LocalDateTime.now());

        String emailBody = String.format(
                "<html>" +
                        "<body>" +
                        "<h2>Hello %s,</h2>" +
                        "<p>A new task submission has been made. Below are the details:</p>" +
                        "<table border='1' style='border-collapse: collapse; width: 100%%;'>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task ID</th>" +
                        "<td style='padding: 8px;'>%d</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task Name</th>" +
                        "<td style='padding: 8px;'>%s</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Task Description</th>" +
                        "<td style='padding: 8px;'>%s</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Submitted By</th>" +
                        "<td style='padding: 8px;'>%s</td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>GitHub Link</th>" +
                        "<td style='padding: 8px;'><a href='%s'>%s</a></td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Deployed URL</th>" +
                        "<td style='padding: 8px;'><a href='%s'>%s</a></td></tr>" +
                        "<tr><th style='padding: 8px; text-align: left; background-color: #f2f2f2;'>Submission Time</th>" +
                        "<td style='padding: 8px;'>%s</td></tr>" +
                        "</table>" +
                        "<p>Best regards,<br>ADMIN</p>" +
                        "</body>" +
                        "</html>",
                assigneeUser.getUsername(), // Assuming User has a getUsername() method
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                assignedUser.getUsername(),
                githubLink, githubLink,
                deployedUrl, deployedUrl,
                sub.getSubmissionTime().toString()
        );

        emailService.sendSimpleMessage(assigneeUser.getEmail(), "Task Submission", emailBody, "ADMIN");

        return submissionRepository.save(sub);
    }


    @Override
    public Submission getTaskSubmission(Long id) {
        return submissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Submission> getTaskSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> getTaskSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    @Override
    public Submission acceptDecline(Long taskId, String status,String jwt) throws Exception {
        Submission sub = getTaskSubmission(taskId);
        sub.setStatus(status);
        if(status.equals("ACCEPT")){
            taskService.completeTask(sub.getId(),jwt);
        }
        return submissionRepository.save(sub);
    }

    @Override
    public Submission updateTaskSubmission(Long Id, Submission submission) throws Exception {
        Submission sub = getTaskSubmission(Id);
        if(submission.getGithubLink()!=null){
            sub.setGithubLink(submission.getGithubLink());
        }
        return submissionRepository.save(sub);
    }

    @Override
    public void deleteTaskSubmission(Long submissionId) {
        Submission sub = getTaskSubmission(submissionId);
        submissionRepository.delete(sub);

    }
}

package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.external.task.TaskDto;
import com.roczyno.submissionservice.external.task.TaskService;
import com.roczyno.submissionservice.external.user.User;
import com.roczyno.submissionservice.external.user.UserService;
import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.repository.SubmissionRepository;
import com.roczyno.submissionservice.util.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl  implements SubmissionService{
    private final SubmissionRepository submissionRepository;
    private final TaskService taskService;
    private final EmailService emailService;
    private final UserService userService;

    @Override
    public Submission submitTask(Long taskId, String githubLink, String jwt, String deployedUrl) {
        TaskDto task = taskService.getTask(taskId, jwt);
        User assigneeUser = userService.getUserById(task.getAssigneeUserId(), jwt);
        User assignedUser = userService.getUserProfile(jwt);
        Submission sub = new Submission();
        sub.setTaskId(task.getId());
        sub.setUserId(assignedUser.getId());
        sub.setUsername(assignedUser.getUsername());
        sub.setGithubLink(githubLink);
        sub.setDeployedUrl(deployedUrl);
        sub.setSubmissionTime(LocalDate.now());

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
                assigneeUser.getUsername(), 
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                assignedUser.getUsername(),
                githubLink, githubLink,
                deployedUrl, deployedUrl,
                sub.getSubmissionTime().toString()
        );

		try {
			emailService.sendSimpleMessage(assigneeUser.getEmail(), "Task Submission", emailBody,
					"Task Manager");
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

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
    public Submission acceptDecline(Long id, String status, String jwt)  {
        Submission sub = getTaskSubmission(id);

        TaskDto task = taskService.completeTask(sub.getTaskId(),jwt);

        User assignedUser = userService.getUserById(task.getAssignedUserId(), jwt);
        sub.setStatus(status);

        if ("ACCEPT".equalsIgnoreCase(status)) {
            taskService.completeTask(sub.getId(), jwt);
            String emailBody = String.format(
                    "<html>" +
                            "<body>" +
                            "<h2>Task Submission Status Update</h2>" +
                            "<p>Dear %s,</p>" +
                            "<p>Your recent task submission has been accepted. Congratulations.</p>" +
                            "<p>Best regards,<br>ADMIN</p>" +
                            "</body>" +
                            "</html>",
                    assignedUser.getUsername()

            );
			try {
				emailService.sendSimpleMessage(assignedUser.getEmail(), "Task Status Update", emailBody,
						"Task manager");
			} catch (MessagingException | UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else if ("REJECTED".equalsIgnoreCase(status)) {
            String emailBody = String.format(
                    "<html>" +
                            "<body>" +
                            "<h2>Task Submission Status Update</h2>" +
                            "<p>Dear %s,</p>" +
                            "<p>Your recent task submission has been rejected. Please redo the task.</p>" +
                            "<p>Best regards,<br>ADMIN</p>" +
                            "</body>" +
                            "</html>",
                    assignedUser.getUsername()

            );
			try {
				emailService.sendSimpleMessage(assignedUser.getEmail(), "Task Status Update", emailBody,
						"Task manager");
			} catch (MessagingException | UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        return submissionRepository.save(sub);
    }


    @Override
    public Submission updateTaskSubmission(Long Id, Submission submission) {
        Submission sub = getTaskSubmission(Id);
        if(submission.getGithubLink()!=null){
            sub.setGithubLink(submission.getGithubLink());
        }
        return submissionRepository.save(sub);
    }

    @Override
    public String deleteTaskSubmission(Long submissionId) {
        Submission sub = getTaskSubmission(submissionId);
        submissionRepository.delete(sub);
        return "submission deleted";

    }
}

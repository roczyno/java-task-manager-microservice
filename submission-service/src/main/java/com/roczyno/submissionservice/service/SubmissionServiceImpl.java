package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.external.task.TaskDto;
import com.roczyno.submissionservice.external.task.TaskService;
import com.roczyno.submissionservice.external.user.User;
import com.roczyno.submissionservice.external.user.UserService;
import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.rabbitmq.SubmissionProducer;
import com.roczyno.submissionservice.repository.SubmissionRepository;
import com.roczyno.submissionservice.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl  implements SubmissionService{
    private final SubmissionRepository submissionRepository;
    private final TaskService taskService;
    private final EmailService emailService;
    private final UserService userService;
	private final SubmissionProducer submissionProducer;

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

		Submission savedSubmission = submissionRepository.save(sub);
		submissionProducer.sendSubmissionMessage(assigneeUser.getUsername(),
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				assignedUser.getUsername(),
				githubLink,
				deployedUrl,
				sub.getSubmissionTime().toString());

		return savedSubmission;
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
			submissionProducer.sendAcceptedMessage(assignedUser.getUsername());
		} else if ("REJECTED".equalsIgnoreCase(status)) {
			submissionProducer.sendDeclinedMessage(assignedUser.getUsername());
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

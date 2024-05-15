package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.external.task.TaskDto;
import com.roczyno.submissionservice.external.task.TaskService;
import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl  implements SubmissionService{
    private final SubmissionRepository submissionRepository;
    private final TaskService taskService;


    @Override
    public Submission submitTask(Long taskId, Long userId, String githubLink,String jwt,String deployedUrl) throws Exception {
        TaskDto task=taskService.getTask(taskId,jwt);
        if(task!=null){
        Submission sub = new Submission();
        sub.setTaskId(task.getId());
        sub.setUserId(userId);
        sub.setGithubLink(githubLink);
        sub.setDeployedUrl(deployedUrl);
        sub.setSubmissionTime(LocalDateTime.now());
        return submissionRepository.save(sub);
        }
        throw new Exception("Task not found");
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

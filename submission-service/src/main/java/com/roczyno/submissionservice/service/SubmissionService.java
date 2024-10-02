package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.model.Submission;

import java.util.List;

public interface SubmissionService {
    Submission submitTask(Long taskId, String githubLink,String jwt,String deployedUrl);
    Submission getTaskSubmission(Long id);
    List<Submission> getTaskSubmissions();
    List<Submission> getTaskSubmissionsByTaskId(Long taskId);
    Submission acceptDecline(Long id, String status,String jwt);
    Submission updateTaskSubmission(Long id, Submission submission);
    String deleteTaskSubmission(Long submissionId);
}

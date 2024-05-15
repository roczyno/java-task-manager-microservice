package com.roczyno.submissionservice.service;

import com.roczyno.submissionservice.model.Submission;

import java.util.List;

public interface SubmissionService {
    Submission submitTask(Long taskId,Long userId, String githubLink,String jwt,String deployedurl) throws Exception;
    Submission getTaskSubmission(Long id);
    List<Submission> getTaskSubmissions();
    List<Submission> getTaskSubmissionsByTaskId(Long taskId);
    Submission acceptDecline(Long taskId, String status,String jwt) throws Exception;
    Submission updateTaskSubmission(Long taskId, Submission submission) throws Exception;
    void deleteTaskSubmission(Long submissionId);
}

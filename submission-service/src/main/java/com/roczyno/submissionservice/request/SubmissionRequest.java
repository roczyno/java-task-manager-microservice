package com.roczyno.submissionservice.request;

import lombok.Data;

@Data
public class SubmissionRequest {
    private String githubLink;
    private String deployedUrl;
}

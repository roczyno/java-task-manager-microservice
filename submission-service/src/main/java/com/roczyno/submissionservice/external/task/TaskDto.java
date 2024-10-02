package com.roczyno.submissionservice.external.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedUserId;
    private Long assigneeUserId;
    private String image;
    private List<String> tags;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
}

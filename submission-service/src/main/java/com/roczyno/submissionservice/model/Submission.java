package com.roczyno.submissionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long taskId;
    private Long userId;
    private String username;
    private String status="PENDING";
    private String githubLink;
    private String deployedUrl;
    private LocalDate submissionTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submission that = (Submission) o;
        return Objects.equals(id, that.id) && Objects.equals(taskId, that.taskId)
                && Objects.equals(userId, that.userId) && Objects.equals(username, that.username)
                && Objects.equals(status, that.status) && Objects.equals(githubLink, that.githubLink)
                && Objects.equals(deployedUrl, that.deployedUrl)
                && Objects.equals(submissionTime, that.submissionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskId, userId, username, status, githubLink, deployedUrl, submissionTime);
    }
}

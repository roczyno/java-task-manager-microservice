package com.roczyno.taskservice.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedUserId;
    private Long assigneeUserId;
    private String image;
    @ElementCollection
    private List<String> tags= new ArrayList<>();
    private LocalDateTime deadline;
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title)
                && Objects.equals(description, task.description) && status == task.status
                && Objects.equals(assignedUserId, task.assignedUserId) &&
                Objects.equals(assigneeUserId, task.assigneeUserId) && Objects.equals(image, task.image)
                && Objects.equals(tags, task.tags) && Objects.equals(deadline, task.deadline)
                && Objects.equals(createdAt, task.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, assignedUserId, assigneeUserId, image, tags,
                deadline, createdAt);
    }
}

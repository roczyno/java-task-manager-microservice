 package com.roczyno.submissionservice.external.task;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SUBMISSION-SERVICE",url="http://submission:8082/")
public interface TaskService {
    @GetMapping("/api/v1/task/{id}")
    TaskDto getTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt);
    @PutMapping("/api/v1/task/{id}/complete")
    TaskDto completeTask(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt);

}

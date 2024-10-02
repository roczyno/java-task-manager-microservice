package com.roczyno.submissionservice.controller;

import com.roczyno.submissionservice.model.Submission;
import com.roczyno.submissionservice.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {
	private final SubmissionService submissionService;

	@PostMapping("/submit")
	public ResponseEntity<Submission> submitTask(@RequestHeader("Authorization") String jwt, @RequestParam Long taskId,
												 @RequestParam String githubLink,
												 @RequestParam String deployedUrl
	) {
		return ResponseEntity.ok(submissionService.submitTask(taskId, githubLink, jwt, deployedUrl));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
		return ResponseEntity.ok(submissionService.getTaskSubmission(id));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Submission>> getAllSubmissions() {
		return ResponseEntity.ok(submissionService.getTaskSubmissions());
	}

	@GetMapping("/all/task/{id}")
	public ResponseEntity<List<Submission>> getAllSubmissionsOfTask(@PathVariable Long id) {
		return ResponseEntity.ok(submissionService.getTaskSubmissionsByTaskId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Submission> acceptOrDeclineSubmission(@RequestHeader("Authorization") String jwt, @PathVariable Long id,
																@RequestParam String status) {
		return ResponseEntity.ok(submissionService.acceptDecline(id, status, jwt));
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Submission> updateSubmission(@PathVariable Long id, @RequestBody Submission submission){
		return ResponseEntity.ok(submissionService.updateTaskSubmission(id,submission));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSubmission(@PathVariable Long id){
		return ResponseEntity.ok(submissionService.deleteTaskSubmission(id));
	}

}

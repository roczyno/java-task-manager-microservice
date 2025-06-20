package com.roczyno.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;

@SpringBootApplication
@RestController
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@GetMapping("/fallback/task")
	public ResponseEntity<String> taskServiceFallback(@RequestParam(required = false) String path) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		
		response.put("timestamp", LocalDateTime.now().toString());
		response.put("status", "SERVICE_UNAVAILABLE");
		response.put("message", "Task Service is currently unavailable. Please try again later.");
		response.put("path", path != null ? path : "/api/v1/task");
		response.put("error", "Circuit Breaker Activated");
		
		return ResponseEntity
			.status(HttpStatus.SERVICE_UNAVAILABLE)
			.contentType(MediaType.APPLICATION_JSON)
			.body(response.toString());
	}

	@GetMapping("/fallback/user")
	public ResponseEntity<String> userServiceFallback(@RequestParam(required = false) String path) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		
		response.put("timestamp", LocalDateTime.now().toString());
		response.put("status", "SERVICE_UNAVAILABLE");
		response.put("message", "User Service is currently unavailable. Please try again later.");
		response.put("path", path != null ? path : "/api/v1/user");
		response.put("error", "Circuit Breaker Activated");
		
		return ResponseEntity
			.status(HttpStatus.SERVICE_UNAVAILABLE)
			.contentType(MediaType.APPLICATION_JSON)
			.body(response.toString());
	}

	@GetMapping("/fallback/submission")
	public ResponseEntity<String> submissionServiceFallback(@RequestParam(required = false) String path) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		
		response.put("timestamp", LocalDateTime.now().toString());
		response.put("status", "SERVICE_UNAVAILABLE");
		response.put("message", "Submission Service is currently unavailable. Please try again later.");
		response.put("path", path != null ? path : "/api/v1/submissions");
		response.put("error", "Circuit Breaker Activated");
		
		return ResponseEntity
			.status(HttpStatus.SERVICE_UNAVAILABLE)
			.contentType(MediaType.APPLICATION_JSON)
			.body(response.toString());
	}
}

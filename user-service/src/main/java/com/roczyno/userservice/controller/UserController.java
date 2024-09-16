package com.roczyno.userservice.controller;

import com.roczyno.userservice.model.User;
import com.roczyno.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
		return ResponseEntity.ok(userService.findUserProfileByJwt(jwt));
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUses());
	}
}

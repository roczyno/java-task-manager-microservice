package com.roczyno.userservice.controller;

import com.roczyno.userservice.request.AuthRequest;
import com.roczyno.userservice.request.RegistrationRequest;
import com.roczyno.userservice.response.AuthResponse;
import com.roczyno.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationservice;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest req) {
       return ResponseEntity.ok(authenticationservice.register(req));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse res= authenticationservice.login(req);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

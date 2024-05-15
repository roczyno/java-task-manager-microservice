package com.roczyno.userservice.controller;

import com.roczyno.userservice.model.User;
import com.roczyno.userservice.request.AuthRequest;
import com.roczyno.userservice.response.AuthResponse;
import com.roczyno.userservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final AuthenticationService authenticationservice;

    public AuthenticationController(AuthenticationService authenticationservice) {
        this.authenticationservice = authenticationservice;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User req) throws Exception {
        User user = authenticationservice.register(req);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) throws Exception {
        AuthResponse res= authenticationservice.login(req);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

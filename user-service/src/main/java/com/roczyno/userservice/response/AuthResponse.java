package com.roczyno.userservice.response;

import com.roczyno.userservice.model.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private boolean status;
    private String message;
    private User user;
}

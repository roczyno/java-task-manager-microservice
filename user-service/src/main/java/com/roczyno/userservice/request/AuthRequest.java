package com.roczyno.userservice.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}

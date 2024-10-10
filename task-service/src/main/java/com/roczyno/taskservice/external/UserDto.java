package com.roczyno.taskservice.external;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private String specialization;
    private String profilePic;
}

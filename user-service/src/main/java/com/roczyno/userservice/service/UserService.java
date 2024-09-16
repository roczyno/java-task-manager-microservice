package com.roczyno.userservice.service;

import com.roczyno.userservice.model.User;

import java.util.List;


public interface UserService {
    User findUserProfileByJwt(String jwt);
    User findUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUses();
}

package com.roczyno.userservice.service;

import com.roczyno.userservice.model.User;

import java.util.List;


public interface UserService {
    User findUserProfileByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;

    User getUserById(Long id);

    List<User> getAllUses();
}

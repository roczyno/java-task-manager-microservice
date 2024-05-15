package com.roczyno.userservice.service;

import com.roczyno.userservice.config.JwtProvider;
import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public UserServiceImpl(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromToken(jwt);
        User user=findUserByEmail(email);
        if(user==null){
            throw new Exception("User with email" +email+ " not found");
        }
        return user;
    }
    @Override
    public User findUserByEmail(String email) throws Exception {
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User with email not found");
        }
        return user;
    }
}

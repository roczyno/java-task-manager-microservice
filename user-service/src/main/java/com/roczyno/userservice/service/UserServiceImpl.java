package com.roczyno.userservice.service;

import com.roczyno.userservice.config.JwtProvider;
import com.roczyno.userservice.exception.UserException;
import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Override
    public User findUserProfileByJwt(String jwt)  {
        String email=jwtProvider.getEmailFromToken(jwt);
		return findUserByEmail(email);
    }
    @Override
    public User findUserByEmail(String email){
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("User with email not found");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public List<User> getAllUses() {
        return userRepository.findAll();
    }
}

package com.roczyno.userservice.service;

import com.roczyno.userservice.config.JwtProvider;
import com.roczyno.userservice.exception.UserException;
import com.roczyno.userservice.model.Role;
import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import com.roczyno.userservice.request.AuthRequest;
import com.roczyno.userservice.request.RegistrationRequest;
import com.roczyno.userservice.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;


    public String register(RegistrationRequest req)  {
        User emailExist=userRepository.findByEmail(req.getEmail());
        if(emailExist!=null){
            throw new UserException("user with this email already exists");
        }
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setEmail(req.getEmail());
        newUser.setSpecialization(req.getSpecialization());
        newUser.setRole(Role.USER);
         userRepository.save(newUser);

        return "User registered successfully";

    }

    public AuthResponse login(AuthRequest req) {
        String email= req.getEmail();
        String password = req.getPassword();
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw  new UserException("User with this email not found");
        }

        Authentication authentication= authenticate(email, password);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(token);
        res.setMessage("login successful");
        res.setStatus(true);
        res.setUser(user);
        return res;

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);
        if(userDetails ==null){
            throw new BadCredentialsException("Invalid username");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Wrong credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}

package com.roczyno.taskservice.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserService {
    @GetMapping("/api/v1/user/profile")
    @CircuitBreaker(name = "userBreaker",fallbackMethod = "userBreakerFallback")
     UserDto getUserProfile(@RequestHeader("Authorization") String jwt);
    @GetMapping("/api/v1/user/{id}")
     UserDto getUserById(@PathVariable Long id,@RequestHeader("Authorization") String jwt);
    default UserDto userBreakerFallback(@RequestHeader("Authorization") String jwt,Throwable exception){
        System.out.println("exception took place:"+ exception.getMessage());
       return new UserDto();
    }
}

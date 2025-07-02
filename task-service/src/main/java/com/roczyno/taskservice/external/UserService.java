package com.roczyno.taskservice.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "${user-service.url}")
public interface UserService {
    @GetMapping("/api/v1/user/profile")
     UserDto getUserProfile(@RequestHeader("Authorization") String jwt);
    @GetMapping("/api/v1/user/{id}")
     UserDto getUserById(@PathVariable Long id,@RequestHeader("Authorization") String jwt);

}

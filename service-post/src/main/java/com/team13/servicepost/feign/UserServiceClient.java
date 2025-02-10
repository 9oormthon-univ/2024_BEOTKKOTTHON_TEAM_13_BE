package com.team13.servicepost.feign;

import com.team13.servicepost.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "service-user")
public interface UserServiceClient {
    @GetMapping("/users")
    ResponseEntity<UserDto> getUserById(@RequestHeader("X-User-Id") Long userId);
}
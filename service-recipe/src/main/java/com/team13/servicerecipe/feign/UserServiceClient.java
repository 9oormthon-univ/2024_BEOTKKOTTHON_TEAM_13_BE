package com.team13.servicerecipe.feign;

import com.team13.servicerecipe.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-user")
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    ResponseEntity<User> getUserById(@PathVariable("id") Long id);
}
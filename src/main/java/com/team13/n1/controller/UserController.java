package com.team13.n1.controller;

import com.team13.n1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("signup")
    public String signUp(@RequestBody Map<String, String> request) {
        service.addUser(request.get("user_id"), request.get("nickname"));
        return "ok";
    }

    @PostMapping("signin")
    public String signIn(@RequestBody Map<String, String> request) {
        if (service.existsById(request.get("user_id"))) {
            return "ok";
        }
        return "";
    }
}

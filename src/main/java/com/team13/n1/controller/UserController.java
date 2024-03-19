package com.team13.n1.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserController {
    @PostMapping("signup")
    public String signUp(@RequestBody Map<String, String> request) {
        log.info(request.get("user_id"));
        return "ok";
    }

    @PostMapping("signin")
    public String signIn(@RequestBody Map<String, String> request) {
        return UUID.randomUUID().toString();
    }
}

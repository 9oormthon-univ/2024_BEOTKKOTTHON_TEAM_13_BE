package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.service.SignInService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    @Value("${app.jwt.keep}")
    private int tokenKeepDuration;

    private final SignInService signInService;

    @GetMapping
    public String index() { return "Index page of service-user"; }

    // 로그인 요청
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody Map<String, String> loginInfo,
                                         HttpServletResponse response) {
        String token = "";
        if (loginInfo.containsKey("user_id") && loginInfo.containsKey("password")) {
            token = signInService.login(loginInfo.get("user_id"), loginInfo.get("password"));
        }

        Cookie cookie = new Cookie("LTK", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenKeepDuration);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public ResponseDto serviceConnectionTest() {
        return new ResponseDto("Greeting!!");
    }

}
package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.service.SignInService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
        // 존재하는 계정이라면, 로그인 토큰을 쿠키에 저장하여 전송함
        if (signInService.verifyLoginInfo(loginInfo)) {
            String token = signInService.createToken(loginInfo.get("email"));

            // 토큰 쿠키 생성
            Cookie cookie = new Cookie("LTK", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(tokenKeepDuration);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        }

        // 존재하지 않는 계정인 경우, 클라이언트로 401 코드를 전송함
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public ResponseDto serviceConnectionTest() {
        return new ResponseDto("Greeting!!");
    }

}
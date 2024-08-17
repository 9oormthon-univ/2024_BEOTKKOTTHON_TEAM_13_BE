package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.service.SignInService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
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
    // 로그인 성공 시 클라이언트에게 JWT 토큰을 반환함
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody Map<String, String> loginInfo,
                                         HttpServletResponse response) {

        // 사용자가 email과 password를 모두 전송했는지 확인
        if (loginInfo.containsKey("email") && loginInfo.containsKey("password")) {

            // 사용자가 전송한 유저 데이터가 DB에 있는 데이터인지 확인
            if (signInService.verifyLoginInfo(loginInfo.get("email"), loginInfo.get("password"))) {

                // 유저 이메일로 유저 정보를 가져옴
                User user = signInService.getUserByEmail(loginInfo.get("email"));

                // JWT 토큰 문자열 생성
                String token = signInService.createTokenByUser(user);

                // JWT 토큰 쿠키 생성
                Cookie cookie = new Cookie("LTK", token);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(tokenKeepDuration);
                cookie.setPath("/");
                response.addCookie(cookie);

                return ResponseEntity.ok().build();
            }

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
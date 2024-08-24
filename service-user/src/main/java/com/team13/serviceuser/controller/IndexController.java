package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.JoinRequest;
import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.service.SignInService;
import com.team13.serviceuser.service.SignUpService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.ObjectError;
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
    private final SignUpService signUpService;

    @GetMapping
    public String index() { return "Index page of service-user"; }

    // 회원가입 요청
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequest joinRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + "; " + message2)
                    .orElse("Validation failed.");
            return ResponseEntity.badRequest().body(errorMessages);
        }
        if (signUpService.checkEmailDuplicate(joinRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이메일이 중복됩니다.");
        }

        if (signUpService.checkNicknameDuplicate(joinRequest.getNickname())) {
            return ResponseEntity.badRequest().body("닉네임이 중복됩니다.");
        }

        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        signUpService.join(joinRequest);
        return ResponseEntity.ok("회원가입 성공");
    }


    // 로그인 요청
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody Map<String, String> loginInfo,
                                         HttpServletResponse response) {
        // 존재하는 계정이라면, 로그인 토큰을 쿠키에 저장하여 전송함
        if (signInService.verifyLoginInfo(loginInfo)) {
            String token = signInService.createToken(loginInfo.get("user_id"));

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
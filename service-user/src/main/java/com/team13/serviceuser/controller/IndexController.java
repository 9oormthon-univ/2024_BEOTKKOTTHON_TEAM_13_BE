package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.JoinRequest;
import com.team13.serviceuser.dto.LoginRequest;
import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.service.SignInService;
import com.team13.serviceuser.service.SignUpService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final SignInService signInService;
    private final SignUpService signUpService;

    @GetMapping
    public String index() { return "Index page of service-user"; }

    //회원가입 요청
    // 회원가입 요청
    @PostMapping("/join")
        public ResponseEntity<String> join (@Valid @RequestBody JoinRequest joinRequest,
                                            BindingResult bindingResult) {
        //request전달 값 조건 에러시 메세지
            if (bindingResult.hasErrors()) {
                String errorMessages = bindingResult.getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .reduce((message1, message2) -> message1 + "; " + message2)
                        .orElse("Validation failed.");
                return ResponseEntity.badRequest().body(errorMessages);
            }

            //이메일 중복 체크
            if (signUpService.checkEmailDuplicate(joinRequest.getEmail())) {
                return ResponseEntity.badRequest().body("이메일이 중복됩니다.");
            }

            // 닉네임 중복 체크
            if (signUpService.checkNicknameDuplicate(joinRequest.getNickname())) {
                return ResponseEntity.badRequest().body("닉네임이 중복됩니다.");
            }

            // password와 passwordCheck가 같은지 체크
            if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }

            signUpService.join(joinRequest);
            return ResponseEntity.ok("회원가입 성공");
        }

    // 로그인 요청
    // 로그인 성공 시 클라이언트에게 JWT 토큰을 반환함
    @PostMapping("/login")
    public  ResponseEntity<String> login (@RequestBody LoginRequest loginRequest,
                                          HttpServletResponse response) {
        User user = signInService.login(loginRequest);
        if (user == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cookie cookie = signInService.createCookieFromUser(user);
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
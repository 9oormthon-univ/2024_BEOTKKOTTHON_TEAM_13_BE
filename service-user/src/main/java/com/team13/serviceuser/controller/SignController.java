package com.team13.serviceuser.controller;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.apiPyaload.code.status.ErrorStatus;
import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.service.SignInService;
import com.team13.serviceuser.service.SignUpService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {

    private final SignInService signInService;
    private final SignUpService signUpService;

    //회원가입 요청
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Object>> join(@Valid @RequestBody SignRequest.RegisterRequestDto registerRequestDto,
                                                    BindingResult bindingResult) {
        // Request 전달 값 조건 에러시 메시지
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + "; " + message2)
                    .orElse("Validation failed.");
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(ErrorStatus.REGISTER4001.getCode(), ErrorStatus.REGISTER4001.getMessage(), errorMessages));
        }

        // 이메일 중복 체크
        if (signUpService.checkEmailDuplicate(registerRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(ErrorStatus.REGISTER4002.getCode(), ErrorStatus.REGISTER4002.getMessage(), null));
        }

        // 닉네임 중복 체크
        if (signUpService.checkNicknameDuplicate(registerRequestDto.getNickname())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(ErrorStatus.REGISTER4003.getCode(), ErrorStatus.REGISTER4003.getMessage(), null));
        }

        // 비밀번호와 비밀번호 확인 일치 여부 체크
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getPasswordCheck())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(ErrorStatus.REGISTER4004.getCode(), ErrorStatus.REGISTER4004.getMessage(), null));
        }

        signUpService.registerUser(registerRequestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("회원가입 성공"));
    }

    // 로그인 요청
    // 로그인 성공 시 클라이언트에게 JWT 토큰을 반환함
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody SignRequest.LoginRequestDto loginRequestDto,
                                                     BindingResult bindingResult,
                                                     HttpServletResponse response) {
        // Request 전달 값 조건 에러시 메시지 (단일 메시지만 반환)
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors()
                    .stream()
                    .findFirst()
                    .map(ObjectError::getDefaultMessage)
                    .orElse("Validation failed.");
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), errorMessage));
        }

        ApiResponse<User> apiResponse = signInService.login(loginRequestDto);

        if (!apiResponse.getIsSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.onFailure(apiResponse.getCode(), apiResponse.getMessage(), null)
            );
        }

        // 로그인 성공 시 쿠키 생성
        User user = (User) apiResponse.getResult();
        signInService.createCookieFromUser(user, response);

        return ResponseEntity.ok(ApiResponse.onSuccess("로그인 성공"));
    }

}

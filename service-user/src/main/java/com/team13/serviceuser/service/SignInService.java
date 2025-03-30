package com.team13.serviceuser.service;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.apiPyaload.code.status.ErrorStatus;
import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class SignInService {
    private final Key jwtSecretKey; // JWT 토큰을 위한 암호키
    private final long tokenKeepDuration; // 토큰 유지 기간(밀리초)
    private final UserRepository userRepository;

    //비밀번호 인코딩
    private final BCryptPasswordEncoder encoder;

    // NOTE: 쿠키 Samesite 옵션
    @Value("${app.cookie.same-site}")
    private String cookieSameSite;

    // NOTE: 쿠키 Secure 옵션
    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    // NOTE: 쿠키 도메인
    @Value("${app.cookie.domain}")
    private String cookieDomain;

    public SignInService(@Value("${app.jwt.secret}") String tokenSecret,
                         @Value("${app.jwt.keep}") long tokenKeepDuration,
                         UserRepository userRepository, BCryptPasswordEncoder encoder) {

        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);

        // 토큰 유지 기간
        this.tokenKeepDuration = tokenKeepDuration * 1000; // 초를 밀리초로 변환

        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public ApiResponse<User> login(SignRequest.LoginRequestDto req) {
        // 이메일 유효성 검사
        Optional<User> optionalUser = userRepository.findByEmail(req.getEmail());
        if (optionalUser.isEmpty()) {
            return ApiResponse.onFailure(ErrorStatus.LOGIN4002.getCode(), ErrorStatus.LOGIN4002.getMessage(), null);
        }
        User user = optionalUser.get();
        // 비밀번호 유효성 검사
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return ApiResponse.onFailure(ErrorStatus.LOGIN4001.getCode(), ErrorStatus.LOGIN4001.getMessage(), null);
        }
        // 로그인 성공 시
        return ApiResponse.onSuccess(user);
    }

    // JWT 쿠키 생성
    public void createCookieFromUser(User user, HttpServletResponse response) {
        // 현재 시간 가져오기
        long now = (new Date()).getTime();

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("userId", user.getId().toString())  // 사용자 ID
                .claim("userNickname", user.getNickname()) // 닉네임
                .setExpiration(new Date(now + tokenKeepDuration)) // 만료 시간 설정
                .signWith(jwtSecretKey, SignatureAlgorithm.HS384)
                .compact();

        log.debug("Generated JWT: {}", token);

        // 기존 LTK 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("LTK", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .domain(cookieDomain)
                .maxAge(0) // 즉시 만료
                .sameSite(cookieSameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        // 새로운 LTK 쿠키 생성
        ResponseCookie newCookie = ResponseCookie.from("LTK", token)
                .httpOnly(true)
                .secure(cookieSecure)  // NOTE: false(DEV), true(OP)
                .path("/")
                .domain(cookieDomain)  // NOTE: "localhost"(DEV), "n1.junyeong.dev"(OP)
                .maxAge(tokenKeepDuration / 1000)
                .sameSite(cookieSameSite) // NOTE: Strict(DEV), None(OP)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

        log.debug("LTK Cookie set with new JWT token.");
    }

    // JWT 쿠키 삭제 메서드 추가
    public void clearAuthCookie(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("LTK", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .domain(cookieDomain)
                .maxAge(0) // 즉시 만료
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        log.debug("LTK Cookie cleared (User logged out).");
    }



}

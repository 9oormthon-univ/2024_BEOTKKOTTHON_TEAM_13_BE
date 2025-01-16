package com.team13.serviceuser.service;

import com.team13.serviceuser.dto.LoginRequestDto;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    public User login(LoginRequestDto req) {
        //email에 대한 유효성 확인
        Optional<User> optionalUser = userRepository.findByEmail(req.getEmail());
        if (optionalUser.isEmpty()) {
            System.out.println("Login ID not found");
            return null;
        }
        User user = optionalUser.get();
        System.out.println("User found: " + user.getEmail());

        //비밀번호에 대한 유효성
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            System.out.println("Password does not match");
            return null;
        }

        System.out.println("Password matches");
        return user;
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
        Cookie deleteCookie = new Cookie("LTK", null);
        deleteCookie.setMaxAge(0); // 즉시 만료
        deleteCookie.setHttpOnly(true); // 동일한 옵션 설정
        deleteCookie.setSecure(true);  // 동일한 옵션 설정
        deleteCookie.setPath("/");     // 경로 설정 (루트 경로)
        deleteCookie.setDomain("localhost"); // 도메인 설정
        response.addCookie(deleteCookie);

        // 새로운 LTK 쿠키 생성
        Cookie newCookie = new Cookie("LTK", token);
        newCookie.setHttpOnly(true);
        newCookie.setSecure(true); // HTTPS 환경에서만 전달
        newCookie.setPath("/"); // 루트 경로
        newCookie.setDomain("localhost"); // 도메인 설정
        newCookie.setMaxAge((int) tokenKeepDuration / 1000); // 초 단위 만료 시간
        response.addCookie(newCookie);

        log.debug("LTK Cookie set with new JWT token.");
    }



}

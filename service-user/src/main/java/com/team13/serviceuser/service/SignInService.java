package com.team13.serviceuser.service;

import com.team13.serviceuser.dto.LoginRequestDto;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

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
    public Cookie createCookieFromUser(User user) {
        // 쿠키 만료 시간 설정을 위한 현재 시간 데이터
        long now = (new Date()).getTime();

        // JWT 토큰 생성
        String token = Jwts.builder()
                            .claim("userId", user.getId().toString())  // ID는 개발 편의상 문자열로 저장
                            .claim("userNickname", user.getNickname())
                            .setExpiration(new Date(now + tokenKeepDuration))
                            .signWith(jwtSecretKey)
                            .compact();

        // JWT 쿠키 생성
        Cookie cookie = new Cookie("LTK", token);
        cookie.setMaxAge((int) tokenKeepDuration / 1000);
        cookie.setPath("/");

        return cookie;
    }
}

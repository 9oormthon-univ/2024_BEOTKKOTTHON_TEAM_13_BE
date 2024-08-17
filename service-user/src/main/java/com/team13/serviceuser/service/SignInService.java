package com.team13.serviceuser.service;

import com.team13.serviceuser.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class SignInService {
    private final Key jwtSecretKey; // JWT 토큰을 위한 암호키

    private final int tokenKeepDuration; // 토큰 유지 기간(밀리초)

    public SignInService(@Value("${app.jwt.secret}") String tokenSecret,
                         @Value("${app.jwt.keep}") int tokenKeepDuration) {

        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);

        // 토큰 유지 기간
        this.tokenKeepDuration = tokenKeepDuration * 1000; // 초를 밀리초로 변환
    }

    // 유저 로그인 정보 검증
    public boolean verifyLoginInfo(String userEmail, String userPassword) {

        // TODO: 유저 인증 메커니즘 추가 (아래는 테스트용 코드)
        if (userEmail.equals("ypjun100@gmail.com") ||
                userEmail.equals("ypjun101@gmail.com")) {
            return true;
        }

        return false;
    }


    // 유저 정보 반환
    public User getUserByEmail(String userEmail) {

        // TODO: 유저 이메일로 유저 정보 가져오는 코드 추가 (아래는 테스트용 코드)
        if (userEmail.equals("ypjun100@gmail.com")) {

            return User.builder()
                    .id(1L)
                    .nickname("ypjun100")
                    .build();

        } else if (userEmail.equals("ypjun101@gmail.com")) {

            return User.builder()
                    .id(2L)
                    .nickname("ypjun101")
                    .build();

        }

        return User.builder().build();
    }


    // JWT 쿠키 생성
    public Cookie createCookieFromUser(User user) {

        // 쿠키 만료 시간 설정을 위한 현재 시간 데이터
        long now = (new Date()).getTime();

        // JWT 토큰 생성
        String token = Jwts.builder()
                .claim("userId", user.getId())
                .claim("userNickname", user.getNickname())
                .setExpiration(new Date(now + tokenKeepDuration))
                .signWith(jwtSecretKey)
                .compact();

        // JWT 쿠키 생성
        Cookie cookie = new Cookie("LTK", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenKeepDuration);
        cookie.setPath("/");

        return cookie;

    }
}

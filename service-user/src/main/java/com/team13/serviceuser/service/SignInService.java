package com.team13.serviceuser.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class SignInService {
    private final Key tokenKey; // JWT 토큰을 위한 키

    private final long tokenKeepDuration; // 토큰 유지 기간(밀리초)

    public SignInService(@Value("${app.jwt.secret}") String tokenSecret,
                         @Value("${app.jwt.keep}") long tokenKeepDuration) {

        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.tokenKey = Keys.hmacShaKeyFor(keyBytes);

        // 토큰 유지 기간
        this.tokenKeepDuration = tokenKeepDuration * 1000; // 초를 밀리초로 변환
    }

    // 로그인
    public String login(String userId, String password) {
        return createToken(userId);
    }

    // 토큰 생성
    private String createToken(String userId) {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("userId", userId)
                .setExpiration(new Date(now + tokenKeepDuration))
                .signWith(tokenKey)
                .compact();
    }
}

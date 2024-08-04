package com.team13.serviceuser.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

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

    // 유저 로그인 정보 검증
    public boolean verifyLoginInfo(Map<String, String> loginInfo) {
        if (loginInfo.containsKey("email") && loginInfo.containsKey("password")) {
            // 유저 인증 메커니즘 추가
            return true;
        }
        return false;
    }

    // 토큰 생성
    public String createToken(String email) {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("email", email)
                .setExpiration(new Date(now + tokenKeepDuration))
                .signWith(tokenKey)
                .compact();
    }
}

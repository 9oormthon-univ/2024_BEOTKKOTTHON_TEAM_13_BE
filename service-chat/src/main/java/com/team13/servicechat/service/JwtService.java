package com.team13.servicechat.service;

import com.team13.servicechat.dto.JwtPayloadDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Log4j2
@Component
public class JwtService {

    // JWT 토큰을 위한 암호키
    @Value("${app.jwt.secret}")
    private final Key jwtSecretKey;

    @Autowired
    public JwtService(@Value("${app.jwt.secret}") String tokenSecret) {
        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    // 올바른 JWT 토큰인지 검증
    // token 문자열은 "LTK=" 접두사가 없어야 함
    public boolean verifyToken(String token) {
        if (token == null) {
            return false;
        }

        // token 문자열이 빈 문자열이 아닌 경우에만 검증 수행
        if (!token.trim().isEmpty()) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(jwtSecretKey)
                        .build()
                        .parseClaimsJws(token);

                return true;
            } catch (Exception e) {
                log.error(e);
            }
        }

        return false;
    }


    // 토큰 문자열에서 페이로드를 추출함
    // 입력되는 문자열은 verifyToken()으로 사전에 검증된 문자열이어야 함
    // "LTK=" 접두사가 없어야 함
    public JwtPayloadDto getPayloadFromToken(String token) {
        JwtPayloadDto jwtPayloadDto = JwtPayloadDto.builder().build();

        try {
            // JWT 토큰에서 claims 내용을 추출함
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // JWT 토큰 페이로드를 추출하여 Dto에 삽입함
            jwtPayloadDto.setUserId(claims.get("userId", String.class));
            jwtPayloadDto.setUserId(claims.get("userEmail", String.class));

        } catch (Exception e) {
            log.error(e);
        }

        return jwtPayloadDto;
    }

}

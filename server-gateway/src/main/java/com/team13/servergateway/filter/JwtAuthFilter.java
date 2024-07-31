package com.team13.servergateway.filter;

import com.team13.servergateway.util.RouterValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final Key tokenKey; // JWT 토큰을 위한 키

    private final RouterValidator validator; // 로그인 토큰 path 검증자


    @Autowired
    public JwtAuthFilter(@Value("${app.jwt.secret}") String tokenSecret,
                         RouterValidator validator) {
        super(Config.class);

        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.tokenKey = Keys.hmacShaKeyFor(keyBytes);

        // 로그인 토큰이 필요한 url path인지 확인을 위한 클래스
        this.validator = validator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 로그인 쿠키가 필요한 URL path인지 확인
            if (validator.isSecured(request)) {
                try {
                    // 요청의 헤더에서 쿠키를 가져옴
                    List<String> cookies = request.getHeaders().get(HttpHeaders.COOKIE);

                    // 만약 쿠키가 없는 경우 예외 발생
                    if (cookies == null || cookies.isEmpty()) {
                        throw new RuntimeException();
                    }

                    // 애플리케이션에서 사용하는 쿠키는 로그인 쿠키밖에 없으므로, 첫 번째 쿠키를 가져옴
                    // 쿠키 앞에 붙은 접두사 'LTK='를 제거
                    String token = cookies.get(0).substring(4);

                    // 해당 쿠키의 내용을 토대로 복호화 수행
                    Jwts.parserBuilder()
                            .setSigningKey(tokenKey)
                            .build()
                            .parseClaimsJws(token);

                } catch (Exception e) {
                    // 만약 try 문 내의 과정 수행 중에 예외가 발생한 경우 인증에 문제가 있다고 판단하고
                    // HTTP 상태 코드를 401로 설정하고, 서비스에 접근할 수 없도록 함
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }

            // 정상 유저인 경우 실행됨
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { }));
        };
    }

    public static class Config { }
}

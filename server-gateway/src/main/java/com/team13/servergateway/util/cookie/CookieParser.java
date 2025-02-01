package com.team13.servergateway.util.cookie;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CookieParser {
    // 쿠키 리스트에서 쿠키를 찾아 반환함
    public static Optional<Cookie> findCookieInCookies(List<String> cookies, String cookieName) {
        // NOTE: 쿠키 리스트의 각 쿠키에 적용
        for (var cookie : cookies) {
            // NOTE: 만약 여러 개의 쿠키가 하나의 쿠키에 포함된 경우 이를 분리함
            List<String> splitCookies = Arrays.stream(cookie.split(";")).toList();

            for (var splitCookie : splitCookies) {
                Cookie _cookie = new Cookie(splitCookie.trim()); // NOTE: 쿠키 데이터의 좌우 공백을 제거함

                if (_cookie.getName().equals(cookieName)) {
                    return Optional.of(_cookie);
                }
            }
        }
        return Optional.empty();
    }
}

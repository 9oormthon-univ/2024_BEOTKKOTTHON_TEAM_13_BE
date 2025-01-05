package com.team13.servergateway.util.cookie;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CookieParser {
    // 쿠키 리스트에서 쿠키를 찾아 반환함
    public static Optional<Cookie> findCookieInCookies(List<String> cookies, String cookieName) {
        for (var cookie : cookies) {
            Cookie _cookie = new Cookie(cookie);

            if (_cookie.getName().equals(cookieName)) {
                return Optional.of(new Cookie(cookie));
            }
        }
        return Optional.empty();
    }
}

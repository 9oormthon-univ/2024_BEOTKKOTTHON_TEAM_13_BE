package com.team13.serviceuser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JwtTokenFilter extends OncePerRequestFilter implements ApplicationContextAware {

    private final Key secretKey;
    private ApplicationContext applicationContext;
    private UserService userService;

    public JwtTokenFilter(Key secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (userService == null) {
            userService = applicationContext.getBean(UserService.class);
        }

        System.out.println("JwtTokenFilter: Request received");

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("JwtTokenFilter: No cookies found");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> jwtTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "jwtToken".equals(cookie.getName()))
                .findFirst();

        if (jwtTokenCookie.isEmpty()) {
            System.out.println("JwtTokenFilter: JWT token cookie not found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenCookie.get().getValue();
        System.out.println("JwtTokenFilter: JWT token found - " + token);

        try {
            if (com.team13.serviceuser.JwtTokenUtil.isExpired(token)) {
                System.out.println("JwtTokenFilter: JWT token is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String loginId = com.team13.serviceuser.JwtTokenUtil.getLoginId(token);
            System.out.println("JwtTokenFilter: Login ID from token - " + loginId);

            User loginUser = userService.getLoginUserByLoginId(loginId);

            if (loginUser != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginUser.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginUser.getRole().name())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("JwtTokenFilter: Authentication set in context");
            } else {
                System.out.println("JwtTokenFilter: User not found");
            }

        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.out.println("JwtTokenFilter: JWT signature does not match locally computed signature");
        } catch (Exception e) {
            System.out.println("JwtTokenFilter: An error occurred during JWT token processing");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}

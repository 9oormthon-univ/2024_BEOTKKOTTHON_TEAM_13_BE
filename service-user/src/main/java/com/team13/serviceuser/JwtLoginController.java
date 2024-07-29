package com.team13.serviceuser;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller@RequiredArgsConstructor@RequestMapping("/api/users/")public class JwtLoginController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        if (auth != null) {
            User loginUser = userService.getLoginUserByLoginId(auth.getName());
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        if (userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join2(joinRequest);
        return "redirect:/api/users/";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        User user = userService.login(loginRequest);

        if (user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        long expireTimeMs = 1000 * 60 * 60;

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), expireTimeMs);

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (expireTimeMs / 1000));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/api/users/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/api/users/";
    }

    @GetMapping("/info")
    public String userInfo(Model model, Authentication auth) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        User loginUser = userService.getLoginUserByLoginId(auth.getName());
        model.addAttribute("user", loginUser);

        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "admin";
    }

    @GetMapping("/authentication-fail")
    public String authenticationFail(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "errorPage/authenticationFail";
    }

    @GetMapping("/authorization-fail")
    public String authorizationFail(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "errorPage/authorizationFail";
    }
}
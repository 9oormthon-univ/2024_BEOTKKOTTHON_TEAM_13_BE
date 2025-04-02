package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.ResponseDto;
import com.team13.serviceuser.dto.UserDto;
import com.team13.serviceuser.util.RandomUserGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    @GetMapping
    public String index() { return "Index page of service-user"; }

    @GetMapping("/config")
    public String getConfig(@RequestHeader("X-User-Id") String userId,
                            @RequestHeader("X-User-Nickname") String userNickname) {
        log.info(userId);
        log.info(userNickname);

        return configTestString;
    }

    @GetMapping("/service-connection-test")
    public ResponseDto serviceConnectionTest() {
        return new ResponseDto("Greeting!!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        // 임의의 닉네임 생성
        String emailId = RandomUserGenerator.generateEmailId();

        // UserDto 객체 생성
        UserDto userDto = UserDto.builder()
                .id(id)
                .email(RandomUserGenerator.generateEmail(emailId))
                .nickname(RandomUserGenerator.generateNickname())
                .userRating(RandomUserGenerator.generateUserRating())
                .profileImageUrl(RandomUserGenerator.generateProfileImageUrl())
                .build();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

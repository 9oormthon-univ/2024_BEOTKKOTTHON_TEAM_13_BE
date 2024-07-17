package com.team13.servicechat.controller;

import com.team13.servicechat.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final UserFeignClient userFeignClient; // 서비스 간 통신 테스트용 Feign Client

    @GetMapping
    public String index() {
        return "Index page of service-chat";
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public String serviceConnectionTest() {
        return userFeignClient.serviceConnectionTest().data();
    }

}
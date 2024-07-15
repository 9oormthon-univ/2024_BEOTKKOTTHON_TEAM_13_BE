package com.team13.servicepost.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    @GetMapping
    public String index() {
        return "Index page of service-post";
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }
}
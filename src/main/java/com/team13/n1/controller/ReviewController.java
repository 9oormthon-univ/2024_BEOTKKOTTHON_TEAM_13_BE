package com.team13.n1.controller;

import com.team13.n1.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RequestMapping("/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public void post(@RequestBody Map<String, String> request) {
        service.save(request);
    }
}

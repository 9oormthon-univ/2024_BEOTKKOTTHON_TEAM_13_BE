package com.team13.n1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @GetMapping("list")
    public List<Map<String, String>> list() {
        List<Map<String, String>> result = new ArrayList<>();

        return new ArrayList<>();
    }
}

package com.team13.n1.controller;

import com.team13.n1.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @GetMapping("list")
    public List<Map<String, Object>> list(@RequestParam("bcode") String bCode,
                                          @RequestParam("type") String type,
                                          @RequestParam("keyword") String keyword,
                                          @RequestParam("page") String page) {
        return service.getList(bCode, type, keyword, page);
    }

    @GetMapping("{post_id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable("post_id") int postId) {
        return service.getPost(postId);
    }

    @PostMapping
    public String save(@RequestBody Map<String, Object> request) throws ParseException {
        return service.save(request);
    }
}
package com.team13.n1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/chats")
    public String chats() {
        return "chats";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}

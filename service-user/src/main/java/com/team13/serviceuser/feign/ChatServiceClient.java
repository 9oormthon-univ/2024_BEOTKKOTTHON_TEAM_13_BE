package com.team13.serviceuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "service-chat")
public interface ChatServiceClient {
    @PostMapping("/chatroom/join")
    String joinChatroom(@RequestBody Map<String, String> request);
}

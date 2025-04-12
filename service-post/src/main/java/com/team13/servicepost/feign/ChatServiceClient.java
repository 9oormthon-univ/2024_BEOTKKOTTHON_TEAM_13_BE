package com.team13.servicepost.feign;

import com.team13.servicepost.dto.ChatroomCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "service-chat")
public interface ChatServiceClient {
    @PostMapping(value = "/chatroom", consumes = "application/json")
    ResponseEntity<String> createChatroom(@RequestBody ChatroomCreateRequest request);
}

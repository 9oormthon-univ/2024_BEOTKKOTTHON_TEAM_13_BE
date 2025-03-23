package com.team13.servicechat.feign;

import com.team13.servicechat.dto.UserDto;
import com.team13.servicechat.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 서비스 간 통신 테스트용 Feign Client
@FeignClient(name = "service-chat", url = "http://localhost:7234")
public interface UserServiceClient {
    @GetMapping("/service-connection-test")
    UserResponseDto serviceConnectionTest();

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}

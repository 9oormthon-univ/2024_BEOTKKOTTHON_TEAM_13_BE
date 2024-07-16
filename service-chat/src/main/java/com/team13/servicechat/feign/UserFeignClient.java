package com.team13.servicechat.feign;

import com.team13.servicechat.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// 서비스 간 통신 테스트용 Feign Client
@FeignClient(name = "service-chat", url = "http://localhost:7234")
public interface UserFeignClient {
    @GetMapping("/service-connection-test")
    UserResponse serviceConnectionTest();
}

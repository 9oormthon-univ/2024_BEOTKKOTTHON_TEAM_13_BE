package com.team13.servicechat.feign;

import com.team13.servicechat.dto.feign.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-post")
public interface PostServiceClient {
    @GetMapping("/{id}")
    PostDTO getPostById(@PathVariable("id") Long id);
}

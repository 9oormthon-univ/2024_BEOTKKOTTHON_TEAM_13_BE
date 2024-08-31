package com.team13.serviceuser.feign;

import com.team13.serviceuser.dto.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "service-post")
public interface PostServiceClient {
    @GetMapping("/posts/user/{userId}")
    List<PostResponseDto> getPostsByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/posts/like/user/{userId}")
    List<PostResponseDto> getLikePostsByUserId(@PathVariable("userId") Long userId);
}

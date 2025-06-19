package com.team13.serviceuser.feign;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.MyPostDto;
import com.team13.serviceuser.dto.MyPostListDto;
import com.team13.serviceuser.dto.MyPostLikeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "service-post", dismiss404 = true)
public interface PostServiceClient {
    @GetMapping("/posts/user")
    Optional<ApiResponse<MyPostListDto<MyPostDto>>> getPostsByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/posts/like/user")
    Optional<ApiResponse<MyPostListDto<MyPostLikeDto>>> getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId, @RequestParam("type") int type);
}

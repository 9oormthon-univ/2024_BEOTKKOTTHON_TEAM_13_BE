package com.team13.serviceuser.feign;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.MyPostDto;
import com.team13.serviceuser.dto.MyPostListDto;
import com.team13.serviceuser.dto.MyPostLikeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-post")
public interface PostServiceClient {
    @GetMapping("/posts/user")
    ApiResponse<MyPostListDto<MyPostDto>> getPostsByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/posts/like/user")
    ApiResponse<MyPostListDto<MyPostLikeDto>> getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId, @RequestParam("type") int type);
}

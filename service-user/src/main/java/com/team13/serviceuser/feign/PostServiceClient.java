package com.team13.serviceuser.feign;

import com.team13.serviceuser.dto.MyPostResponseDto;
import com.team13.serviceuser.dto.MypagePostListResponseDto;
import com.team13.serviceuser.dto.MypagePostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "service-post")
public interface PostServiceClient {
    @GetMapping("/posts/user")
    MypagePostListResponseDto<MyPostResponseDto> getPostsByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/posts/like/user")
    List<MypagePostResponseDto> getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId);
}

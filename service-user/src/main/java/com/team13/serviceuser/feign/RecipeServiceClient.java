package com.team13.serviceuser.feign;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.MypageRecipeListResponseDto;
import com.team13.serviceuser.dto.MypageRecipeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "service-recipe")
public interface RecipeServiceClient {

    @GetMapping("/recipes/user")
    ApiResponse<MypageRecipeListResponseDto> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/recipes/like/user")
    ApiResponse<MypageRecipeListResponseDto> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId);
}

package com.team13.serviceuser.feign;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.MyRecipeListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "service-recipe", dismiss404 = true)
public interface RecipeServiceClient {

    @GetMapping("/recipes/user")
    Optional<ApiResponse<MyRecipeListDto>> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/recipes/like/user")
    Optional<ApiResponse<MyRecipeListDto>> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId);
}

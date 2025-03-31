package com.team13.serviceuser.feign;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.MyRecipeListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "service-recipe")
public interface RecipeServiceClient {

    @GetMapping("/recipes/user")
    ApiResponse<MyRecipeListDto> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/recipes/like/user")
    ApiResponse<MyRecipeListDto> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId);
}

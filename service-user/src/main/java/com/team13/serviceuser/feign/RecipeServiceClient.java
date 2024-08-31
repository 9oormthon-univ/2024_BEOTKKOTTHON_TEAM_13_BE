package com.team13.serviceuser.feign;

import com.team13.serviceuser.dto.MypageRecipeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "service-recipe")
public interface RecipeServiceClient {

    @GetMapping("/recipes/user/{userId}")
    List<MypageRecipeResponseDto> getRecipesByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/recipes/like/user/{userId}")
    List<MypageRecipeResponseDto> getLikeRecipesByUserId(@PathVariable("userId") Long userId);
}

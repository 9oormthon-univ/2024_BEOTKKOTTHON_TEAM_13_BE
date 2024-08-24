package com.team13.servicerecipe.controller;

import com.team13.servicerecipe.dto.RecipeDto;
import com.team13.servicerecipe.feign.UserFeignClient;
import com.team13.servicerecipe.util.RandomRecipeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final UserFeignClient userFeignClient;

    @GetMapping
    public String index() {
        return "Index page of service-recipe";
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public String serviceConnectionTest() {
        return userFeignClient.serviceConnectionTest().data();
    }


    @GetMapping("/brief")
    public List<RecipeDto> brief() {
        List<RecipeDto> recipes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            recipes.add(RecipeDto.builder()
                            .id(RandomRecipeGenerator.id())
                            .title(RandomRecipeGenerator.title())
                            .thumbnailImagePath(RandomRecipeGenerator.thumbnailImagePath())
                            .build());
        }

        return recipes;
    }


    @GetMapping("/list")
    public ResponseEntity<List<RecipeDto>> list(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                @RequestParam(value = "page", defaultValue = "1") String page,
                                                @RequestParam(value = "categories", defaultValue = "") String categories) {
        List<RecipeDto> recipes = new ArrayList<>();

        for (int i = 0; i < 20; i ++) {
            recipes.add(RecipeDto.builder()
                            .id(RandomRecipeGenerator.id())
                            .title(RandomRecipeGenerator.title())
                            .thumbnailImagePath(RandomRecipeGenerator.thumbnailImagePath())
                            .likesCount(RandomRecipeGenerator.likesCount())
                            .build());
        }

        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

}
package com.team13.n1.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Log4j2
@RequestMapping("/recipe")
public class RecipeController {
    @GetMapping("brief")
    public List<Map<String, String>> getBriefRecipe() {
        List<Map<String, String>> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Map<String, String> recipe = new HashMap<>();

            int id = new Random().nextInt(10);
            String[] name = {"수제버거", "유부초밥", "가지 스테이크", "돼지고기 김치찌개", "잡채", "한방닭죽", "카레치즈라면", "달걀미트볼", "계란찜", "배추닭국"};
            recipe.put("recipe_id", Integer.toString(id));
            recipe.put("recipe_thumbnail_image", "/user-image/recipe/" + id + "/" + "thumbnail.png");
            recipe.put("recipe_name", name[id]);
            result.add(recipe);
        }

        return result;
    }
}

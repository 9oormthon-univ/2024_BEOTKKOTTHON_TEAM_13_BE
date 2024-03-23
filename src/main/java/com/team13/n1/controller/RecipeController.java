package com.team13.n1.controller;

import com.team13.n1.dto.RecipeCreateRequest;
import com.team13.n1.service.RecipeCreateService;
import com.team13.n1.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Log4j2
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService service;
    private final RecipeCreateService createService;
    @GetMapping("brief")
    public List<Map<String, Object>> briefRecipe() {
        return service.getBriefList();
    }

    @GetMapping("list")
    public List<Map<String, Object>> list(@RequestParam("keyword") String keyword,
                                          @RequestParam("page") String page) {
        return service.getList(keyword, page);
    }

    @GetMapping("{recipe_id}")
    public Map<String, Object> recipe(@PathVariable("recipe_id") int recipeId,
                                      @RequestParam("bcode") String bCode) {
        return service.getRecipeById(recipeId, bCode);
    }

    @PostMapping("/creat")
    public ResponseEntity<String> createRecipe(@RequestBody RecipeCreateRequest request) {
        createService.createRecipe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");

    }







}



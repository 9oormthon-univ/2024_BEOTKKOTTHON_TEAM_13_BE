package com.team13.n1.controller;

import com.team13.n1.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@Log4j2
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService service;

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

    @PostMapping
    public ResponseEntity<String> createRecipe(@RequestBody Map<String, Object> request) {
        try {
            String message = service.saveRecipe(request);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (ParseException e) {
            return new ResponseEntity<>("Failed to parse request.", HttpStatus.BAD_REQUEST);
        }
    }

}

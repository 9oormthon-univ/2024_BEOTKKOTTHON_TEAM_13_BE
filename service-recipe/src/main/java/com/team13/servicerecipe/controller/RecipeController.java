package com.team13.servicerecipe.controller;

import com.team13.servicerecipe.dto.RecipeIngredientDTO;
import com.team13.servicerecipe.dto.RecipeProcessDTO;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.entity.RecipeProcess;
import com.team13.servicerecipe.service.RecipeProcessService;
import com.team13.servicerecipe.service.RecipeService;
import com.team13.servicerecipe.service.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeProcessService recipeProcessService;

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        boolean userExists = recipeService.checkUserExists(recipe.getUserId());
        if (userExists) {
            return new ResponseEntity<>(recipeService.saveRecipe(recipe), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<RecipeWithUserDetails> getRecipeById(@PathVariable Long id) {
//        return recipeService.getRecipeWithUserDetails(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//    }

    @GetMapping("/{recipeId}/ingredients")
    public ResponseEntity<List<RecipeIngredientDTO>> getIngredientByRecipeId(@PathVariable Long recipeId) {
        List<RecipeIngredient> ingredient = recipeIngredientService.getIngredientsByRecipeId(recipeId);
        List<RecipeIngredientDTO> ingredientDTO = ingredient.stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ingredientDTO, HttpStatus.OK);
    }

    @GetMapping("/{recipeId}/processes")
    public ResponseEntity<List<RecipeProcessDTO>> getProcessByRecipeId(@PathVariable Long recipeId) {
        List<RecipeProcess> processs = recipeProcessService.getProcessByRecipeId(recipeId);
        List<RecipeProcessDTO> processsDTO = processs.stream()
                .map(recipeProcessService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(processsDTO, HttpStatus.OK);
    }

//    @PostMapping("/{recipeId}/processes")
//    public ResponseEntity<RecipeProcess> addProcess(@PathVariable Long recipeId, @RequestBody RecipeProcess process) {
//        Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);
//        if (recipe.isPresent()) {
//            process.setRecipe(recipe.get());
//            return new ResponseEntity<>(RecipeProcessService.saveProcess(process), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }


}
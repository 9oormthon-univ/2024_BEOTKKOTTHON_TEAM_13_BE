package com.team13.servicerecipe.controller;

import com.team13.servicerecipe.dto.RecipesIngredientsDTO;
import com.team13.servicerecipe.dto.RecipesProcessesDTO;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipesIngredients;
import com.team13.servicerecipe.entity.RecipesProcesses;
import com.team13.servicerecipe.service.RecipeService;
import com.team13.servicerecipe.service.RecipesIngredientsService;
import com.team13.servicerecipe.service.RecipesProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipesProcessesService RecipesProcessesService;

    @Autowired
    private  RecipesIngredientsService recipesIngredientsService;

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
    public ResponseEntity<List<RecipesIngredientsDTO>> getIngredientsByRecipeId(@PathVariable Long recipeId) {
        List<RecipesIngredients> ingredient = recipesIngredientsService.getIngredientsByRecipeId(recipeId);
        List<RecipesIngredientsDTO> ingredientDTO = ingredient.stream()
                .map(recipesIngredientsService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ingredientDTO, HttpStatus.OK);
    }

    @GetMapping("/{recipeId}/processes")
    public ResponseEntity<List<RecipesProcessesDTO>> getProcessByRecipeId(@PathVariable Long recipeId) {
        List<RecipesProcesses> processs = RecipesProcessesService.getProcessByRecipeId(recipeId);
        List<RecipesProcessesDTO> processsDTO = processs.stream()
                .map(RecipesProcessesService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(processsDTO, HttpStatus.OK);
    }

    @PostMapping("/{recipeId}/processes")
    public ResponseEntity<RecipesProcesses> addProcess(@PathVariable Long recipeId, @RequestBody RecipesProcesses process) {
        Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);
        if (recipe.isPresent()) {
            process.setRecipe(recipe.get());
            return new ResponseEntity<>(RecipesProcessesService.saveProcess(process), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}

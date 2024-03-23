package com.team13.n1.service;
import com.team13.n1.dto.RecipeCreateRequest;
import com.team13.n1.dto.RecipeIngredientDto;
import com.team13.n1.dto.RecipeProcessDto;
import com.team13.n1.entity.Recipe;
import com.team13.n1.entity.RecipeIngredient;
//import com.team13.n1.entity.RecipeProcess;
import com.team13.n1.repository.RecipeIngredientRepository;
//import com.team13.n1.repository.RecipeProcessRepository;
import com.team13.n1.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeCreateService {
    private final RecipeRepository recipeRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

//    private final RecipeProcessRepository recipeProcessRepository;

    @Autowired
    public RecipeCreateService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository =recipeRepository;
        this.recipeIngredientRepository =recipeIngredientRepository;
//        this.recipeProcessRepository = recipeProcessRepository;
    }

    // RecipeCreateService의 createRecipe 메서드는 void로 변경합니다.
    public void createRecipe(RecipeCreateRequest request) {




            Recipe recipe = new Recipe();
            recipe.setThumbnailImage("data[0]");

            recipe.setTitle("data[2]");
            recipe.setLikesCount(5);
            recipe.setCommentsCount(1);

        recipe.setUserId("abc");
        recipe.setThumbnailImage(request.getThumbnailImage());

        recipe.setTitle(request.getTitle());
        // Set other fields accordingly

        Recipe savedRecipe = recipeRepository.save(recipe);

        if(request.getIngredients()!=null) {
            for(RecipeIngredientDto ingredientDto : request.getIngredients()){
                RecipeIngredient ingredient = convertToEntity(ingredientDto);
                ingredient.setRecipe(savedRecipe);
                recipeIngredientRepository.save(ingredient);
            }
        }

//        if(request.getProcesses() !=null) {
//            for(RecipeProcessDto ingredientDto : request.getIngredients()) {
//                RecipeProcess ingredient = convertToEntity(ingredientDto);
//                ingredient.setRecipe(savedRecipe);
//                recipeProcessRepository.save(ingredient);
//            }
//        }

        // Set processes

    }
    private RecipeIngredient convertToEntity(RecipeIngredientDto ingredientDto) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setAmount(ingredientDto.getAmount());
        // Set other fields if needed
        return ingredient;
    }

//    private RecipeProcess convertToEntity(RecipeProcessDto processDto) {
//        RecipeProcess process = new RecipeProcess();
//        process.setImage(processDto.getImage());
//        process.setContents(processDto.getContents());
//        // Set other fields if needed
//        return process;
//    }
}

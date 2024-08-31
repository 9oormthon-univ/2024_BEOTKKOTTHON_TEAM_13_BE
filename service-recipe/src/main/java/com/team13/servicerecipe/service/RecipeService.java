package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.*;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.entity.RecipeProcess;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {


    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    @Autowired
    private RecipeProcessService recipeProcessService;

    @Autowired
    private LikeRecipeService likeRecipeService;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }


    public  boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() ==  HttpStatus.OK;
    }

    public RecipeResponseDto createRecipeWithDetails(RecipeRequestDto recipeRequestDto, Long userId) {
        boolean userExists = checkUserExists(userId);
        if (!userExists) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Recipe recipe = convertDtoToEntity(recipeRequestDto);
        recipe.setUserId(userId);

        RecipeResponseDto savedRecipeResponse = saveRecipeWithDetails(recipe, recipeRequestDto.getIngredients(), recipeRequestDto.getProcesses());
          return savedRecipeResponse;
    }

    private Recipe convertDtoToEntity (RecipeRequestDto recipeRequestDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeRequestDto.getTitle());
        recipe.setContents(recipeRequestDto.getContents());
        recipe.setThumbnailImagePath(recipeRequestDto.getThumbnailImagePath());
        recipe.setCreatedAt(recipeRequestDto.getCreatedAt());
        recipe.setType(recipeRequestDto.getType());
        return recipe;
    }

    public RecipeResponseDto saveRecipeWithDetails(Recipe recipe, List<RecipeIngredientDto> ingredientsDto, List<RecipeProcessDto> processesDto ) {
        Recipe savedRecipe = saveRecipe(recipe);

        if (ingredientsDto != null & !ingredientsDto.isEmpty()) {
            List<RecipeIngredient> ingredients = ingredientsDto.stream().map(dto -> {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setRecipe(savedRecipe);
                ingredient.setName(dto.getName());
                ingredient.setAmount(dto.getAmount());
                return ingredient;
            }).collect(Collectors.toList());

            ingredients.forEach(recipeIngredientService::saveIngredient);
        }

        if (processesDto !=null & !processesDto.isEmpty()) {
            List<RecipeProcess> processes = processesDto.stream().map(dto -> {
                RecipeProcess process = new RecipeProcess();
                process.setRecipe(savedRecipe);
                process.setContents(dto.getContents());
                process.setImagePath(dto.getImagePath());
                return process;
            }).collect(Collectors.toList());

            processes.forEach(recipeProcessService::saveProcess);
        }

        String userNickname = fetchUserNickname(recipe.getUserId());
        Long likesCount = 0L;


        return buildRecipeResponseDto(savedRecipe, userNickname, likesCount);
    }

    public Optional<RecipeResponseDto> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            return Optional.empty();
        }

        Recipe recipe = recipeOptional.get();
        String userNickname = fetchUserNickname(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipeId);



        return Optional.of(buildRecipeResponseDto(recipe, userNickname, likesCount));
    }

    private String fetchUserNickname(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user nickname.");
        }
        return response.getBody().getNickname();
    }


    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe, String userNickname ,Long likesCount) {
        List<RecipeIngredient> ingredients = recipeIngredientService.getIngredientsByRecipeId(recipe.getId());
        List<RecipeIngredientDto> ingredientDto = ingredients.stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        List<RecipeProcess> processes = recipeProcessService.getProcessByRecipeId(recipe.getId());
        List<RecipeProcessDto> processDto = processes.stream()
                .map(recipeProcessService::convertToDto)
                .collect(Collectors.toList());

        RecipeResponseDto recipeResponseDto = new RecipeResponseDto();
        recipeResponseDto.setId(recipe.getId());
        recipeResponseDto.setUserId(recipe.getUserId());
        recipeResponseDto.setTitle(recipe.getTitle());
        recipeResponseDto.setContents(recipe.getContents());
        recipeResponseDto.setCommentCount(recipe.getCommentCount());
        recipeResponseDto.setThumbnailImagePath(recipe.getThumbnailImagePath());
        recipeResponseDto.setCreatedAt(recipe.getCreatedAt());
        recipeResponseDto.setType(recipe.getType());
        recipeResponseDto.setUserNickname(userNickname);
        recipeResponseDto.setIngredients(ingredientDto);
        recipeResponseDto.setProcesses(processDto);
        recipeResponseDto.setLikesCount(likesCount);

        return recipeResponseDto;
    }

    public List<RecipeResponseDto> getRecipesByUserId(Long userId) {
        List<Recipe> recipes = recipeRepository.findAllByUserId(userId);

        return recipes.stream()
                .map(recipe -> {
                    String userNickname = fetchUserNickname(recipe.getUserId());
                    Long likesCount = likeRecipeService.getLikesCount(recipe.getId());
                    return buildRecipeResponseDto(recipe, userNickname, likesCount);
                })
                .collect(Collectors.toList());
    }

}

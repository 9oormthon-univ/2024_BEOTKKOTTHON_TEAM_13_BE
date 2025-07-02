package com.team13.servicerecipe.service;

import com.team13.servicerecipe.apiPayload.ApiResponse;
import com.team13.servicerecipe.apiPayload.code.status.ErrorStatus;
import com.team13.servicerecipe.apiPayload.code.status.SuccessStatus;
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
    @Autowired
    private RecipeCommentService recipeCommentService;

    /**
     * 레시피 N를 랜덤으로 가져옴
     * @param n - 가져올 레시피 수
     * @return List<RecipeDto>
     */
    public List<RecipeDto> getRandomRecipes(int n) {
        List<Recipe> recipes = recipeRepository.findRandom(n);

        // NOTE: List<Recipe> -> List<RecipeDto>
        return recipes.stream().map((RecipeDto::of)).toList();
    }

    //레시피를 저장
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    //사용자가 존재하는지 확인
    public  boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() ==  HttpStatus.OK;
    }

    //레시피 생성 (세부사항 포함)
    public ApiResponse<RecipeResponseDto> createRecipeWithDetails(RecipeRequestDto recipeRequestDto, Long userId) {
        if (!checkUserExists(userId)) {
            return ApiResponse.onFailure(ErrorStatus.RECIPE_NOT_FOUND.getCode(), ErrorStatus.RECIPE_NOT_FOUND.getMessage(), null);
        }
        Recipe recipe = Recipe.builder()
                .userId(userId)
                .title(recipeRequestDto.getTitle())
                .contents(recipeRequestDto.getContents())
                .thumbnailImagePath(recipeRequestDto.getThumbnailImagePath())
                .build();

        RecipeResponseDto responseDto = saveRecipeWithDetails(recipe, recipeRequestDto);
        return new ApiResponse<>(true, SuccessStatus.RECIPE_CREATED.getCode(), SuccessStatus.RECIPE_CREATED.getMessage(), responseDto);
    }

    //레시피를 저장하고, 해당 레시피의 재료 및 과정 정보도 저장
    public RecipeResponseDto saveRecipeWithDetails(Recipe recipe, RecipeRequestDto dto) {
        Recipe savedRecipe = saveRecipe(recipe);

        List<RecipeIngredient> ingredients = dto.getIngredients().stream()
                .map(ingredientDto -> RecipeIngredientDto.toEntity(ingredientDto, savedRecipe))
                .collect(Collectors.toList());
        ingredients.forEach(recipeIngredientService::saveIngredient);

        List<RecipeProcess> processes = dto.getProcesses().stream()
                .map(processDto -> RecipeProcessDto.toEntity(processDto, savedRecipe))
                .collect(Collectors.toList());
        processes.forEach(recipeProcessService::saveProcess);

        return buildRecipeResponseDto(savedRecipe);
    }

    //특정 레시피 ID를 기반으로 사용자 정보를 포함한 레시피 DTO 조회
    public ApiResponse<RecipeResponseDto> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            return ApiResponse.onFailure(ErrorStatus.RECIPE_NOT_FOUND.getCode(), ErrorStatus.RECIPE_NOT_FOUND.getMessage(), null);
        }
        Recipe recipe = recipeOptional.get();
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipeId);
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByRecipeId(recipeId);

        List<RecipeIngredientDto> ingredients = recipeIngredientService.getIngredientsByRecipeId(recipe.getId())
                .stream().map(RecipeIngredientDto::from).collect(Collectors.toList());

        List<RecipeProcessDto> processes = recipeProcessService.getProcessByRecipeId(recipe.getId())
                .stream().map(RecipeProcessDto::from).collect(Collectors.toList());

        return ApiResponse.onSuccess(RecipeResponseDto.from(recipe, userDto, ingredients, processes, comments));
    }

    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe) {
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        List<RecipeIngredientDto> ingredients = recipeIngredientService.getIngredientsByRecipeId(recipe.getId())
                .stream().map(RecipeIngredientDto::from).collect(Collectors.toList());
        List<RecipeProcessDto> processes = recipeProcessService.getProcessByRecipeId(recipe.getId())
                .stream().map(RecipeProcessDto::from).collect(Collectors.toList());

        return RecipeResponseDto.from(recipe, userDto, ingredients, processes, null);
    }

    // 사용자 정보를 조회
    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user details.");
        }
        return response.getBody();
    }


    // 특정 사용자의 레시피 목록을 가져오는 메소드 (마이페이지에서 사용)
    public MyRecipeListDto getRecipesByUserId(Long userId) {
        UserDto userDto = fetchUserDetails(userId);
        List<MyRecipeDto> recipeList = recipeRepository.findAllByUserId(userId)
                .stream().map(MyRecipeDto::from).collect(Collectors.toList());
        return MyRecipeListDto.from(userDto, recipeList);
    }

    // 사용자가 좋아요한 레시피 목록을 가져오는 메소드
    public MyRecipeListDto getLikeRecipesByUserId(Long userId) {
        UserDto userDto = fetchUserDetails(userId);
        List<Long> likedRecipeIds = likeRecipeService.getLikedRecipeIdsByUserId(userId);
        List<MyRecipeDto> recipeList = likedRecipeIds.stream()
                .map(recipeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MyRecipeDto::from)
                .collect(Collectors.toList());
        return MyRecipeListDto.from(userDto, recipeList);
    }
}

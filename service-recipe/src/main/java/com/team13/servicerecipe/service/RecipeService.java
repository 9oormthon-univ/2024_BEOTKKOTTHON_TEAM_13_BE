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
        if (!checkUserExists(userId)) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Recipe recipe = convertDtoToEntity(recipeRequestDto,userId);
        return saveRecipeWithDetails(recipe, recipeRequestDto);
    }

    private Recipe convertDtoToEntity(RecipeRequestDto dto, Long userId) {
        return Recipe.builder()
                .userId(userId)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .thumbnailImagePath(dto.getThumbnailImagePath())
                .build();
    }

    public RecipeResponseDto saveRecipeWithDetails(Recipe recipe, RecipeRequestDto dto ) {
        Recipe savedRecipe = saveRecipe(recipe);

        List<RecipeIngredient> ingredients = dto.getIngredients().stream()
                .map(ingredientDto -> RecipeIngredient.builder()
                        .name(ingredientDto.getName())
                        .amount(ingredientDto.getAmount())
                        .recipe(savedRecipe)
                        .build())
                .collect(Collectors.toList());
        ingredients.forEach(recipeIngredientService::saveIngredient);

        List<RecipeProcess> processes = dto.getProcesses().stream()
                .map(processDto -> RecipeProcess.builder()
                        .imagePath(processDto.getImagePath())
                        .contents(processDto.getContents())
                        .recipe(savedRecipe)
                        .build())
                .collect(Collectors.toList());
        processes.forEach(recipeProcessService::saveProcess);
        return buildRecipeResponseDto(savedRecipe);
    }

    public Optional<RecipeResponseDto> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            return Optional.empty();
        }
        Recipe recipe = recipeOptional.get();
        String userNickname = fetchUserNickname(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipeId);

        return Optional.of(buildRecipeResponseDto(recipe));
    }

    private String fetchUserNickname(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user nickname.");
        }
        return response.getBody().getNickname();
    }

    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user details.");
        }
        return response.getBody();
    }

    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe) {
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipe.getId());

        List<RecipeIngredientDto> ingredientDtoList =  recipeIngredientService.getIngredientsByRecipeId(recipe.getId()).stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        List<RecipeProcessDto> processDtoList = recipeProcessService.getProcessByRecipeId(recipe.getId()).stream()
                .map(recipeProcessService::convertToDto)
                .collect(Collectors.toList());

        return RecipeResponseDto.builder()
                .id(recipe.getId())
                .userId(recipe.getUserId())
                .title(recipe.getTitle())
                .contents(recipe.getContents())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .ingredients(ingredientDtoList)
                .processes(processDtoList)
                .build();
    }

    public List<MypageRecipeResponseDto> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId).stream()
                .map(this::buildMypageRecipeResponseDto)
                .collect(Collectors.toList());
    }

    public List<MypageRecipeResponseDto> getLikeRecipesByUserId(Long userId) {
        // LikePostService를 사용하여 사용자가 좋아요한 Post ID 리스트를 가져옴
        List<Long> likedRecipeIds = likeRecipeService.getLikedRecipeIdsByUserId(userId);

        // 각 Post ID를 사용하여 Post 엔티티를 조회하고, PostResponseDto로 변환
        return likedRecipeIds.stream()
                .map(recipeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::buildMypageRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private MypageRecipeResponseDto buildMypageRecipeResponseDto(Recipe recipe) {
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipe.getId());

        List<RecipeIngredientDto> ingredientDtoList = recipeIngredientService.getIngredientsByRecipeId(recipe.getId())
                .stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        return MypageRecipeResponseDto.builder()
                .id(recipe.getId())
                .userId(recipe.getUserId())
                .title(recipe.getTitle())
                .contents(recipe.getContents())
                .commentCount(recipe.getCommentCount())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .ingredients(ingredientDtoList)
                .likesCount(likesCount)
                .build();
    }
}

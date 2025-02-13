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

    //레시피를 저장
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    //특정 ID의 레시피 조회
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    //사용자가 존재하는지 확인
    public  boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() ==  HttpStatus.OK;
    }

    //레시피 생성 (세부사항 포함)
    public RecipeResponseDto createRecipeWithDetails(RecipeRequestDto recipeRequestDto, Long userId) {
        if (!checkUserExists(userId)) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Recipe recipe = convertDtoToEntity(recipeRequestDto,userId);
        return saveRecipeWithDetails(recipe, recipeRequestDto);
    }

    //DTO를 엔티티로 변환하는 메소드
    private Recipe convertDtoToEntity(RecipeRequestDto dto, Long userId) {
        return Recipe.builder()
                .userId(userId)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .thumbnailImagePath(dto.getThumbnailImagePath())
                .build();
    }

    //레시피를 저장하고, 해당 레시피의 재료 및 과정 정보도 저장
    public RecipeResponseDto saveRecipeWithDetails(Recipe recipe, RecipeRequestDto dto ) {
        Recipe savedRecipe = saveRecipe(recipe);

        // 재료 저장
        List<RecipeIngredient> ingredients = dto.getIngredients().stream()
                .map(ingredientDto -> RecipeIngredient.builder()
                        .name(ingredientDto.getName())
                        .amount(ingredientDto.getAmount())
                        .recipe(savedRecipe)
                        .build())
                .collect(Collectors.toList());
        ingredients.forEach(recipeIngredientService::saveIngredient);

        // 조리 과정 저장
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

    //특정 레시피 ID를 기반으로 사용자 정보를 포함한 레시피 DTO 조회
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

    //사용자 닉네임을 조회
    private String fetchUserNickname(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user nickname.");
        }
        return response.getBody().getNickname();
    }

    // 사용자 정보를 조회
    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user details.");
        }
        return response.getBody();
    }

    // Recipe 엔티티를 RecipeResponseDto로 변환
    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe) {
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipe.getId());

        // 재료 리스트 변환
        List<RecipeIngredientDto> ingredientDtoList =  recipeIngredientService.getIngredientsByRecipeId(recipe.getId()).stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        // 조리 과정 리스트 변환
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

    // 특정 사용자의 레시피 목록을 가져오는 메소드 (마이페이지에서 사용)
    public List<MypageRecipeResponseDto> getRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId).stream()
                .map(this::buildMypageRecipeResponseDto)
                .collect(Collectors.toList());
    }

    // 사용자가 좋아요한 레시피 목록을 가져오는 메소드
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

    // Recipe 엔티티를 MypageRecipeResponseDto로 변환하는 메소드
    private MypageRecipeResponseDto buildMypageRecipeResponseDto(Recipe recipe) {
        UserDto userDto = fetchUserDetails(recipe.getUserId());
        Long likesCount = likeRecipeService.getLikesCount(recipe.getId());

        // 재료 리스트 변환
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

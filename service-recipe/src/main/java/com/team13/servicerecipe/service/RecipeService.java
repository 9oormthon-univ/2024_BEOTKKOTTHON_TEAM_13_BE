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
import org.apache.catalina.User;
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
    public ApiResponse<RecipeResponseDto> createRecipeWithDetails(RecipeRequestDto recipeRequestDto, Long userId) {
        if (!checkUserExists(userId)) {
            return ApiResponse.onFailure(ErrorStatus.RECIPE_NOT_FOUND.getCode(), ErrorStatus.RECIPE_NOT_FOUND.getMessage(), null);
        }
        Recipe recipe = convertDtoToEntity(recipeRequestDto,userId);
        RecipeResponseDto responseDto = saveRecipeWithDetails(recipe, recipeRequestDto);
        return new ApiResponse<>(true, SuccessStatus.RECIPE_CREATED.getCode(), SuccessStatus.RECIPE_CREATED.getMessage(), responseDto);
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
    public ApiResponse<RecipeResponseDto> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            return ApiResponse.onFailure(ErrorStatus.RECIPE_NOT_FOUND.getCode(), ErrorStatus.RECIPE_NOT_FOUND.getMessage(), null);
        }
        Recipe recipe = recipeOptional.get();

        // 사용자 정보 조회
        UserDto userDto = fetchUserDetails(recipe.getUserId());

        // 좋아요 개수 조회
        Long likesCount = likeRecipeService.getLikesCount(recipeId);

        // 해당 레시피에 달린 댓글 가져오기
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByRecipeId(recipeId);

        // DTO 변환 후 반환
        RecipeResponseDto responseDto = buildRecipeResponseDto(recipe, userDto, comments, likesCount);
        return ApiResponse.onSuccess(responseDto);
    }

    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe, UserDto userDto, List<RecipeCommentDto> comments, Long likesCount) {
        List<RecipeIngredientDto> ingredientDtoList = recipeIngredientService.getIngredientsByRecipeId(recipe.getId())
                .stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        List<RecipeProcessDto> processDtoList = recipeProcessService.getProcessByRecipeId(recipe.getId())
                .stream()
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
                .comments(comments)
                .build();
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
    public MypageRecipeListResponseDto getRecipesByUserId(Long userId) {
        UserDto userDto = fetchUserDetails(userId);

        // 해당 사용자의 레시피 목록 가져오기
        List<MypageRecipeResponseDto> recipeList = recipeRepository.findAllByUserId(userId)
                .stream()
                .map(recipe -> buildMypageRecipeResponseDto(recipe, userDto))
                .collect(Collectors.toList());

        return MypageRecipeListResponseDto.builder()
                .userId(userDto.getId())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .userRating(userDto.getUserRating())
                .recipes(recipeList)
                .build();
    }

    // 사용자가 좋아요한 레시피 목록을 가져오는 메소드
    public MypageRecipeListResponseDto getLikeRecipesByUserId(Long userId) {
        // 사용자 정보를 한 번만 가져오기
        UserDto userDto = fetchUserDetails(userId);

        // 사용자가 좋아요한 레시피 목록 가져오기
        List<Long> likedRecipeIds = likeRecipeService.getLikedRecipeIdsByUserId(userId);

        List<MypageRecipeResponseDto> recipeList = likedRecipeIds.stream()
                .map(recipeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(recipe -> buildMypageRecipeResponseDto(recipe, userDto))
                .collect(Collectors.toList());

        return MypageRecipeListResponseDto.builder()
                .userId(userDto.getId())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .userRating(userDto.getUserRating())
                .recipes(recipeList)
                .build();
    }


    // Recipe 엔티티를 MypageRecipeResponseDto로 변환하는 메소드
    private MypageRecipeResponseDto buildMypageRecipeResponseDto(Recipe recipe, UserDto userDto) {
        return MypageRecipeResponseDto.builder()
                .id(recipe.getId())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .build();
    }
}

package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeCommentDto;
import com.team13.servicerecipe.dto.UserDto;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeComment;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.RecipeCommentRepository;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeCommentService {

    @Autowired
    private RecipeCommentRepository recipeCommentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public RecipeCommentDto addComment(Long recipeId, RecipeCommentDto commentDto, Long userId) {

        RecipeComment comment = RecipeComment.builder()
                .comment(commentDto.getComment())
                .userId(userId)
                .recipe(Recipe.builder().id(recipeId).build())
                .parentComment(commentDto.getParentCommentId() != null ? getCommentById(commentDto.getParentCommentId()) : null)
                .build();

        RecipeComment savedComment = saveComment(comment);
        return convertToDto(savedComment);
    }

    public RecipeComment saveComment(RecipeComment comment) {
        return recipeCommentRepository.save(comment);
    }


    //댓글 테이블 번호로 해당 내용가져오기
    public RecipeComment getCommentById(Long commentId) {
        return recipeCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("해당 댓글 번호를 가진 댓글이 없습니다."));
    }

    //사용자 번호로 해당 사용자가 적은 댓글 모두 불러오기 -> 마이페이지에서 사용 예정
    public List<RecipeCommentDto> getCommentsByUserId(Long userId) {
        return recipeCommentRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //해당 레시피에 적힌 댓글 모두 불러오기
    public List<RecipeCommentDto> getCommentsByRecipeId(Long recipeId) {
        return recipeCommentRepository.findByRecipeId(recipeId).stream().
                map(this::convertToDto).
                collect(Collectors.toList());
    }

    //해당 댓글의 대댓글 확인
    public List<RecipeCommentDto> getRepliesByCommentId(Long commentId) {
        return recipeCommentRepository.findByParentCommentId(commentId).stream().
                map(this::convertToDto).
                collect(Collectors.toList());
    }

    public RecipeCommentDto convertToDto(RecipeComment comment) {
        UserDto userDto = fetchUserDetails(comment.getUserId());
        return RecipeCommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userId(comment.getUserId())
                .recipeId(comment.getRecipe().getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .userNickname(userDto.getNickname())
                .profileImageUrl(userDto.getProfileImageUrl())
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(this::convertToDto).collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }


    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("회원정보 찾기에 실패했습니다.");
        }
        return response.getBody();
    }
}


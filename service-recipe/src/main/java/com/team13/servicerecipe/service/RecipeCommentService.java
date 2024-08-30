package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeCommentDto;
import com.team13.servicerecipe.dto.UserDto;
import com.team13.servicerecipe.entity.RecipeComment;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.RecipeCommentRepository;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeCommentService {

    @Autowired
    private RecipeCommentRepository recipeCommentRepository;

    @Autowired
    private UserServiceClient userServiceClient;

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
        List<RecipeComment> comments = recipeCommentRepository.findByUserId(userId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    //해당 레시피에 적힌 댓글 모두 불러오기
    public List<RecipeCommentDto> getCommentsByRecipeId(Long recipeId) {
        List<RecipeComment> comments = recipeCommentRepository.findByRecipeId(recipeId);
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    //해당 댓글의 대댓글 확인
    public List<RecipeCommentDto> getRepliesByCommentId(Long commentId) {
        List<RecipeComment> replies = recipeCommentRepository.findByParentCommentId(commentId);
        return replies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public RecipeCommentDto convertToDto(RecipeComment comment) {
        RecipeCommentDto dto = new RecipeCommentDto();
        dto.setId(comment.getId());
        dto.setComment(comment.getComment());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUserId(comment.getUserId());
        dto.setRecipeId(comment.getRecipe().getId());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);

        UserDto userDto = fetchUserDetails(comment.getUserId());
        dto.setUserNickname(userDto.getNickname());
        dto.setProfileImageUrl(userDto.getProfileImageUrl());


        List<RecipeCommentDto> replyDtos = comment.getReplies().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        dto.setReplies(replyDtos);

        return dto;
    }

    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("회원정보 찾기에 실패했습니다.");
        }
        return response.getBody();
    }
}
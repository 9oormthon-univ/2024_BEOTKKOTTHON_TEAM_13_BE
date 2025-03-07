package com.team13.servicerecipe.controller;

import com.team13.servicerecipe.apiPayload.ApiResponse;
import com.team13.servicerecipe.dto.RecipeCommentDto;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeComment;
import com.team13.servicerecipe.service.RecipeCommentService;
import com.team13.servicerecipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class RecipeCommentController {

    @Autowired
    private RecipeCommentService recipeCommentService;

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeCommentDto>> addComment(@PathVariable("recipeId") Long recipeId,
                                                                    @RequestBody RecipeCommentDto commentDto,
                                                                    @RequestHeader("X-User-Id") Long userId) {
        ApiResponse<RecipeCommentDto> response = recipeCommentService.addComment(recipeId, commentDto, userId);
        return ResponseEntity.status(response.getIsSuccess() ? 201 : 404).body(response);
    }

    //해당 레시피에 있는 댓글
    @GetMapping("/{recipeId}")
    public ResponseEntity<List<RecipeCommentDto>> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId) {
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByRecipeId(recipeId);
        return comments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(comments);
    }

    //해당 댓글에 달린 답글 확인
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<RecipeCommentDto>> getRepliesByCommentId(@PathVariable("commentId") Long commentId) {
        List<RecipeCommentDto> replies = recipeCommentService.getRepliesByCommentId(commentId);
        return replies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(replies);
    }

    //내가 쓴 댓글 확인
    @GetMapping("/user")
    public ResponseEntity<List<RecipeCommentDto>> getCommentsByUserId(@RequestHeader("X-User-Id") Long userId) {
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByUserId(userId);
        return comments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(comments);
    }



}

package com.team13.servicerecipe.controller;

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
    public ResponseEntity<RecipeCommentDto> addComment(@PathVariable("recipeId") Long recipeId, @RequestBody RecipeCommentDto commentDto) {
        if (recipeService.getRecipeById(recipeId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RecipeCommentDto savedCommentDto = recipeCommentService.addComment(recipeId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommentDto);
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
    @GetMapping("/{userId}")
    public ResponseEntity<List<RecipeCommentDto>> getCommentsByUserId(@PathVariable("userId") Long userId) {
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByUserId(userId);
        return comments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(comments);
    }



}

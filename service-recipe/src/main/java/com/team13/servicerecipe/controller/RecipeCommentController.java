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
@RequestMapping("/comments")public class RecipeCommentController {

    @Autowired
    private RecipeCommentService recipeCommentService;

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/{recipeId}")
    public ResponseEntity<RecipeCommentDto> addComment(@PathVariable Long recipeId, @RequestBody RecipeCommentDto commentDto) {
        Optional<Recipe> recipeOptional = recipeService.getRecipeById(recipeId);
        if (!recipeOptional.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        RecipeComment comment = new RecipeComment();
        comment.setComment(commentDto.getComment());
        comment.setCreatedAt(new Date());
        comment.setUserId(commentDto.getUserId());
        comment.setRecipe(recipeOptional.get());
        if (commentDto.getParentCommentId() != null) {
            RecipeComment parentComment = recipeCommentService.getCommentById(commentDto.getParentCommentId());
            comment.setParentComment(parentComment);
        }

        RecipeComment savedComment = recipeCommentService.saveComment(comment);
        RecipeCommentDto savedCommentDto = recipeCommentService.convertToDto(savedComment);

        return new ResponseEntity<>(savedCommentDto, HttpStatus.CREATED);
    }

    //해당 레시피에 있는 댓글
    @GetMapping("/{recipeId}")
    public ResponseEntity<List<RecipeCommentDto>> getCommentsByRecipeId(@PathVariable Long recipeId) {
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByRecipeId(recipeId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    //해당 댓글에 달린 답글 확인
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<RecipeCommentDto>> getRepliesByCommentId(@PathVariable Long commentId) {
        List<RecipeCommentDto> replies = recipeCommentService.getRepliesByCommentId(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    //내가 쓴 댓글 확인
    @GetMapping("/{userId}")
    public ResponseEntity<List<RecipeCommentDto>> getCommentsByUserId(@PathVariable Long userId) {
        List<RecipeCommentDto> comments = recipeCommentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }



}

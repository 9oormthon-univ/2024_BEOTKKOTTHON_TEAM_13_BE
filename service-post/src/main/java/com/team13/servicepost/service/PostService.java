package com.team13.servicepost.service;

import com.team13.servicepost.dto.*;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;
import com.team13.servicepost.feign.UserServiceClient;
import com.team13.servicepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private  PostIngredientService postIngredientService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private LikePostService likePostService;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public PostResponseDto createPostWithIngredients(PostRequestDto postRequestDto) {
        // Check if user exists
        boolean userExists = checkUserExists(postRequestDto.getUserId());
        if (!userExists) {
            throw new RuntimeException("User does not exist.");
        }

        // Convert PostRequestDto to Post entity
        Post post = convertDtoToEntity(postRequestDto);

        // Save the post entity and its ingredients
        PostResponseDto savedPostResponse = savePostWithIngredients(post, postRequestDto.getIngredients());

        return savedPostResponse;
    }

    private Post convertDtoToEntity(PostRequestDto postRequestDto) {
        Post post = new Post();
        post.setUserId(postRequestDto.getUserId());
        post.setStatus(postRequestDto.getStatus());
        post.setGroupSize(postRequestDto.getGroupSize());
        post.setCurGroupSize(postRequestDto.getCurGroupSize());
        post.setChatId(postRequestDto.getChatId());
        post.setCreatedAt(postRequestDto.getCreatedAt());
        post.setClosedAt(postRequestDto.getClosedAt());
        post.setLocationBcode(postRequestDto.getLocationBcode());
        post.setLocationAddress(postRequestDto.getLocationAddress());
        post.setLocationLongitude(postRequestDto.getLocationLongitude());
        post.setLocationLatitude(postRequestDto.getLocationLatitude());
        post.setTitle(postRequestDto.getTitle());
        post.setPricePerUser(postRequestDto.getPricePerUser());
        post.setType(postRequestDto.getType());
        post.setContents(postRequestDto.getContents());
        return post;
    }

    public PostResponseDto savePostWithIngredients(Post post, List<PostIngredientDto> ingredientsDto) {
        // Save the post entity
        Post savedPost = savePost(post);

        // Convert and save each ingredient
        if (ingredientsDto != null && !ingredientsDto.isEmpty()) {
            List<PostIngredient> ingredients = ingredientsDto.stream().map(dto -> {
                PostIngredient ingredient = new PostIngredient();
                ingredient.setPost(savedPost); // set the post to the savedPost
                ingredient.setName(dto.getName());
                ingredient.setUrl(dto.getUrl());
                return ingredient;
            }).collect(Collectors.toList());

            ingredients.forEach(postIngredientService::saveIngredient);
        }

        // Return the response DTO with the user details and saved data
        return getPostWithUserDetails(savedPost.getId()).orElseThrow(
                () -> new RuntimeException("Failed to retrieve post details after saving"));
    }

    public Optional<PostResponseDto> getPostWithUserDetails(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return Optional.empty();
        }

        Post post = postOptional.get();
        ResponseEntity<UserDto> response = userServiceClient.getUserById(post.getUserId());
        if (response.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        UserDto user = response.getBody();
        if (user == null) {
            return Optional.empty();
        }

        //list 형식으로 같은 postId가진 이미지 불러오기
        List<PostImage> images = postImageService.getImagesByPostId(postId);
        List<PostImageDto> imageDto = images.stream()
                .map(postImageService::convertToDto)
                .collect(Collectors.toList());

        List<PostIngredient> ingredients = postIngredientService.getIngredientsByPostId(postId);
        List<PostIngredientDto> ingredientDto = ingredients.stream()
                .map(postIngredientService::convertToDto)
                .collect(Collectors.toList());

        //좋아요 수 가져오기
        Long likesCount = likePostService.getLikesCount(postId);

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setUserId(post.getUserId());
        postResponseDto.setStatus(post.getStatus());
        postResponseDto.setGroupSize(post.getGroupSize());
        postResponseDto.setCurGroupSize(post.getCurGroupSize());
        postResponseDto.setChatId(post.getChatId());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setClosedAt(post.getClosedAt());
        postResponseDto.setLocationBcode(post.getLocationBcode());
        postResponseDto.setLocationAddress(post.getLocationAddress());
        postResponseDto.setLocationLongitude(post.getLocationLongitude());
        postResponseDto.setLocationLatitude(post.getLocationLatitude());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setPricePerUser(post.getPricePerUser());
        postResponseDto.setType(post.getType());
        postResponseDto.setContents(post.getContents());
        postResponseDto.setUserNickname(user.getNickname());  //feign을 이용한 같은 userId에 대한 nickname불러오기
        postResponseDto.setImages(imageDto); //list형식으로 불러오기
        postResponseDto.setIngredients(ingredientDto);
        postResponseDto.setLikesCount(likesCount);

        return Optional.of(postResponseDto);
    }
}


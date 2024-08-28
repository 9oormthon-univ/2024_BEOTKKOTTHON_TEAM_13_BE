package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.dto.PostIngredientDto;
import com.team13.servicepost.dto.PostResponseDto;
import com.team13.servicepost.dto.UserDto;
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


        return Optional.of(postResponseDto);
    }
}


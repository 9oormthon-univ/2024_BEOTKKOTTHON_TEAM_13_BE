package com.team13.servicepost.service;

import com.team13.servicepost.dto.*;
import com.team13.servicepost.entity.LikePost;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;
import com.team13.servicepost.feign.UserServiceClient;
import com.team13.servicepost.repository.LikePostRepository;
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
    private LikePostRepository likePostRepository;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private PostIngredientService postIngredientService;

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

    // 새 게시물, 재료, 이미지 저장을 처리하는 메서드
    public PostResponseDto createPostWithDetails(PostRequestDto postRequestDto, Long userId) {
        // 사용자가 존재하는지 확인
        boolean userExists = checkUserExists(userId);
        if (!userExists) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        // PostRequestDto를 Post 엔티티로 변환
        Post post = convertDtoToEntity(postRequestDto);
        post.setUserId(userId); // userId 설정

        // 게시물 엔티티, 재료, 이미지 저장
        PostResponseDto savedPostResponse = savePostWithDetails(post, postRequestDto.getIngredients(), postRequestDto.getImages());

        return savedPostResponse;
    }

    private Post convertDtoToEntity(PostRequestDto postRequestDto) {
        Post post = new Post();
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

    public PostResponseDto savePostWithDetails(Post post, List<PostIngredientDto> ingredientsDto, List<PostImageDto> imagesDto) {
        // 게시물 엔티티 저장
        Post savedPost = savePost(post);

        // 각 재료를 변환하고 저장
        if (ingredientsDto != null && !ingredientsDto.isEmpty()) {
            List<PostIngredient> ingredients = ingredientsDto.stream().map(dto -> {
                PostIngredient ingredient = new PostIngredient();
                ingredient.setPost(savedPost);
                ingredient.setName(dto.getName());
                ingredient.setUrl(dto.getUrl());
                return ingredient;
            }).collect(Collectors.toList());

            ingredients.forEach(postIngredientService::saveIngredient);
        }

        // 각 이미지를 변환하고 저장
        if (imagesDto != null && !imagesDto.isEmpty()) {
            List<PostImage> images = imagesDto.stream().map(dto -> {
                PostImage image = new PostImage();
                image.setPost(savedPost);
                image.setImagePath(dto.getImagePath());
                return image;
            }).collect(Collectors.toList());

            images.forEach(postImageService::saveImage);
        }

        // 응답을 위한 사용자 닉네임 가져오기 및 좋아요 수 초기화
        String userNickname = fetchUserNickname(post.getUserId());
        Long likesCount = 0L;  // 새 게시물은 0개의 좋아요로 시작

        // 사용자 세부 정보 및 저장된 데이터를 포함한 응답 DTO 반환
        return buildPostResponseDto(savedPost, userNickname, likesCount);
    }

    public Optional<PostResponseDto> getPostWithUserDetails(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return Optional.empty();
        }

        Post post = postOptional.get();
        String userNickname = fetchUserNickname(post.getUserId());
        Long likesCount = likePostService.getLikesCount(postId);

        // 사용자 세부 정보 및 저장된 데이터를 포함한 응답 DTO 반환
        return Optional.of(buildPostResponseDto(post, userNickname, likesCount));
    }

    private String fetchUserNickname(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user nickname.");
        }
        return response.getBody().getNickname();
    }

    private PostResponseDto buildPostResponseDto(Post post, String userNickname, Long likesCount) {
        // 같은 postId를 가진 이미지를 리스트 형식으로 불러오기
        List<PostImage> images = postImageService.getImagesByPostId(post.getId());
        List<PostImageDto> imageDto = images.stream()
                .map(postImageService::convertToDto)
                .collect(Collectors.toList());

        List<PostIngredient> ingredients = postIngredientService.getIngredientsByPostId(post.getId());
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
        postResponseDto.setUserNickname(userNickname);
        postResponseDto.setImages(imageDto);
        postResponseDto.setIngredients(ingredientDto);
        postResponseDto.setLikesCount(likesCount);

        return postResponseDto;
    }

    public List<PostResponseDto> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        return posts.stream()
                .map(post -> {
                    String userNickname = fetchUserNickname(post.getUserId());
                    Long likesCount = likePostService.getLikesCount(post.getId());
                    return buildPostResponseDto(post, userNickname, likesCount);
                })
                .collect(Collectors.toList());
    }

    // 좋아요한 게시글을 가져오기 위한 메서드
    public List<PostResponseDto> getLikePostsByUserId(Long userId) {
        // LikePostService를 사용하여 사용자가 좋아요한 Post ID 리스트를 가져옴
        List<Long> likedPostIds = likePostService.getLikedPostIdsByUserId(userId);

        // 각 Post ID를 사용하여 Post 엔티티를 조회하고, PostResponseDto로 변환
        return likedPostIds.stream()
                .map(postId -> postRepository.findById(postId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(post -> {
                    String userNickname = fetchUserNickname(post.getUserId());
                    Long likesCount = likePostService.getLikesCount(post.getId());
                    return buildPostResponseDto(post, userNickname, likesCount);
                })
                .collect(Collectors.toList());
    }


}
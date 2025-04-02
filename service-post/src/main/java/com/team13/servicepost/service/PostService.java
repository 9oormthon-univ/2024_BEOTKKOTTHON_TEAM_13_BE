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
    private PostIngredientService postIngredientService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private LikePostService likePostService;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }


    public boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() == HttpStatus.OK;
    }

    // 새 게시물, 재료, 이미지 저장을 처리하는 메서드
    public PostResponseDto createPostWithDetails(PostRequestDto postRequestDto, Long userId) {
        if (!checkUserExists(userId)) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        Post post = postRequestDto.toEntity();
        post.setUserId(userId);

        return savePostWithDetails(post, postRequestDto.getIngredients(), postRequestDto.getImages());
    }


    public PostResponseDto savePostWithDetails(Post post, List<PostIngredientDto> ingredientsDto, List<PostImageDto> imagesDto) {
        Post savedPost = savePost(post);

        if (ingredientsDto != null && !ingredientsDto.isEmpty()) {
            List<PostIngredient> ingredients = ingredientsDto.stream()
                    .map(dto -> PostIngredientDto.toEntity(dto, savedPost))
                    .collect(Collectors.toList());
            ingredients.forEach(postIngredientService::saveIngredient);
        }

        if (imagesDto != null && !imagesDto.isEmpty()) {
            List<PostImage> images = imagesDto.stream()
                    .map(dto -> PostImageDto.toEntity(dto, savedPost))
                    .collect(Collectors.toList());
            images.forEach(postImageService::saveImage);
        }

        return buildPostResponseDto(savedPost);
    }

    public Optional<PostResponseDto> getPostWithUserDetails(Long postId) {
        return postRepository.findById(postId)
                .map(this::buildPostResponseDto);
    }

    private PostResponseDto buildPostResponseDto(Post post) {
        UserDto userDto = fetchUserDetails(post.getUserId());

        List<PostImageDto> imageDto = postImageService.getImagesByPostId(post.getId())
                .stream()
                .map(PostImageDto::from)
                .collect(Collectors.toList());

        List<PostIngredientDto> ingredientDto = postIngredientService.getIngredientsByPostId(post.getId())
                .stream()
                .map(PostIngredientDto::from)
                .collect(Collectors.toList());

        return PostResponseDto.from(post, userDto, ingredientDto, imageDto, likePostService.getLikesCount(post.getId()));
    }

    private UserDto fetchUserDetails(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user details.");
        }
        return response.getBody();
    }

    public MyPostListDto<MyPostDto> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        UserDto userDto = fetchUserDetails(userId);

        List<MyPostDto> postList = posts.stream()
                .map(post -> MyPostDto.from(post,
                        postIngredientService.getIngredientsByPostId(post.getId()).stream().map(PostIngredientDto::from).collect(Collectors.toList()),
                        postImageService.getImagesByPostId(post.getId()).stream().map(PostImageDto::from).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return MyPostListDto.from(userDto, postList);
    }

    //  좋아요한 글 목록
    public MyPostListDto<MyPostLikeDto> getLikePostsResponseByUserId(Long userId, int type) {
        List<Long> likedPostIds = likePostService.getLikedPostIdsByUserId(userId);
        UserDto userDto = fetchUserDetails(userId);

        List<MyPostLikeDto> likedPosts = likedPostIds.stream()
                .map(postRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(post -> post.getType() == type)
                .map(post -> MyPostLikeDto.from(post,
                        postIngredientService.getIngredientsByPostId(post.getId()).stream().map(PostIngredientDto::from).collect(Collectors.toList()),
                        postImageService.getImagesByPostId(post.getId()).stream().map(PostImageDto::from).collect(Collectors.toList()),
                        fetchUserDetails(post.getUserId()).getNickname()
                ))
                .collect(Collectors.toList());

        return MyPostListDto.from(userDto, likedPosts);
    }

}

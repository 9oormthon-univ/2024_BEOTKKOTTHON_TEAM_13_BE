package com.team13.servicepost.service;

import com.team13.servicepost.dto.*;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;
import com.team13.servicepost.exception.ChatroomCreationException;
import com.team13.servicepost.feign.ChatServiceClient;
import com.team13.servicepost.feign.UserServiceClient;
import com.team13.servicepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private ChatServiceClient chatServiceClient;


    public Post savePost(Post post) {
        return postRepository.save(post);
    }


    public boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() == HttpStatus.OK;
    }

    // 새 게시물, 재료, 이미지 저장을 처리하는 메서드
    public PostResponseDto createPostWithDetails(PostRequestDto postRequestDto, Long userId, MultipartFile[] images)
    {
        if (!checkUserExists(userId)) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        Post post = postRequestDto.toEntity();
        post.setUserId(userId);

        Post savedPost = savePost(post);

        // 채팅방 생성
        String chatId = createChatroomForPost(userId, savedPost.getId());
        savedPost.setChatId(chatId);  // chatId 저장
        postRepository.save(savedPost); // 다시 저장 (chatId 추가)

        return savePostWithDetails(savedPost, postRequestDto.getIngredients(), images);
    }


    private String createChatroomForPost(Long userId, Long postId) {
        ChatroomCreateRequest request = new ChatroomCreateRequest(String.valueOf(userId), postId);
        ResponseEntity<String> response = chatServiceClient.createChatroom(request);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();  // chatId
        }

        throw new ChatroomCreationException("채팅방 생성 실패");
    }


    public PostResponseDto savePostWithDetails(Post post, List<PostIngredientDto> ingredientsDto, MultipartFile[] imageFiles) {
        Post savedPost = savePost(post);

        if (ingredientsDto != null && !ingredientsDto.isEmpty()) {
            List<PostIngredient> ingredients = ingredientsDto.stream()
                    .map(dto -> PostIngredientDto.toEntity(dto, savedPost))
                    .collect(Collectors.toList());
            ingredients.forEach(postIngredientService::saveIngredient);
        }

        if (imageFiles != null && imageFiles.length > 0) {
            for (MultipartFile file : imageFiles) {
                postImageService.saveImageFile(file, savedPost);
            }
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

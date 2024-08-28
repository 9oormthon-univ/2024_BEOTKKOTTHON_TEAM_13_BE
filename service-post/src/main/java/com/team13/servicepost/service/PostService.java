package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.dto.PostTestDto;
import com.team13.servicepost.dto.User;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
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
    private UserServiceClient userServiceClient;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public boolean checkUserExists(Long userId) {
        ResponseEntity<User> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public Optional<PostTestDto> getPostWithUserDetails(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return Optional.empty();
        }

        Post post = postOptional.get();
        ResponseEntity<User> response = userServiceClient.getUserById(post.getUserId());
        if (response.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        User user = response.getBody();
        if (user == null) {
            return Optional.empty();
        }

        //list 형식으로 같은 postId가진 이미지 불러오기
        List<PostImage> images = postImageService.getImagesByPostId(postId);
        List<PostImageDto> imageDto = images.stream()
                .map(postImageService::convertToDto)
                .collect(Collectors.toList());

        PostTestDto postTestDto = new PostTestDto();
        postTestDto.setId(post.getId());
        postTestDto.setUserId(post.getUserId());
        postTestDto.setStatus(post.getStatus());
        postTestDto.setGroupSize(post.getGroupSize());
        postTestDto.setCurGroupSize(post.getCurGroupSize());
        postTestDto.setChatId(post.getChatId());
        postTestDto.setCreatedAt(post.getCreatedAt());
        postTestDto.setClosedAt(post.getClosedAt());
        postTestDto.setLocationBcode(post.getLocationBcode());
        postTestDto.setLocationAddress(post.getLocationAddress());
        postTestDto.setLocationLongitude(post.getLocationLongitude());
        postTestDto.setLocationLatitude(post.getLocationLatitude());
        postTestDto.setTitle(post.getTitle());
        postTestDto.setPricePerUser(post.getPricePerUser());
        postTestDto.setType(post.getType());
        postTestDto.setContents(post.getContents());
        postTestDto.setUserNickname(user.getNickname());  //feign을 이용한 같은 userId에 대한 nickname불러오기
        postTestDto.setImages(imageDto); //list형식으로 불러오기

        return Optional.of(postTestDto);
    }
}


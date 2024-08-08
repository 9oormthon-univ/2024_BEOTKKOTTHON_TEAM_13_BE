package com.team13.servicepost.service;

import com.team13.servicepost.client.UserServiceClient;
import com.team13.servicepost.dto.PostWithUserDetails;
import com.team13.servicepost.dto.User;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

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

    public Optional<PostWithUserDetails> getPostWithUserDetails(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return Optional.empty();
        }

        Post post = postOptional.get();
        ResponseEntity<User> response = userServiceClient.getUserById(post.getUsersId());
        if (response.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        User user = response.getBody();
        if (user == null) {
            return Optional.empty();
        }

        PostWithUserDetails postWithUserDetails = new PostWithUserDetails();
        postWithUserDetails.setId(post.getId());
        postWithUserDetails.setUsersId(post.getUsersId());
        postWithUserDetails.setStatus(post.getStatus());
        postWithUserDetails.setGroupSize(post.getGroupSize());
        postWithUserDetails.setCurGroupSize(post.getCurGroupSize());
        postWithUserDetails.setChatId(post.getChatId());
        postWithUserDetails.setCreatedAt(post.getCreatedAt());
        postWithUserDetails.setClosedAt(post.getClosedAt());
        postWithUserDetails.setLocationBcode(post.getLocationBcode());
        postWithUserDetails.setLocationAddress(post.getLocationAddress());
        postWithUserDetails.setLocationLongitude(post.getLocationLongitude());
        postWithUserDetails.setLocationLatitude(post.getLocationLatitude());
        postWithUserDetails.setTitle(post.getTitle());
        postWithUserDetails.setPrice(post.getPrice());
        postWithUserDetails.setPricePerUser(post.getPricePerUser());
        postWithUserDetails.setType(post.getType());
        postWithUserDetails.setContents(post.getContents());
        postWithUserDetails.setUserNickname(user.getNickname()); // Set the user's nickname

        return Optional.of(postWithUserDetails);
    }

}
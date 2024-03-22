package com.team13.n1.service;


import com.team13.n1.entity.Post;
import com.team13.n1.entity.User;
import com.team13.n1.entity.UserLikePost;
import com.team13.n1.repository.PostRepository;
import com.team13.n1.repository.UserLikePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserLikePostServiceImpl implements UserLikePostService {

    @Autowired
    private UserLikePostRepository userLikePostRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getLikedPostsByUser(User user) {
        List<UserLikePost> userLikePosts = userLikePostRepository.findByUser(user);
        List<Integer> likedPostIds = userLikePosts.stream().map(UserLikePost::getPostId).collect(Collectors.toList());
        return postRepository.findAllById(likedPostIds);
    }
}
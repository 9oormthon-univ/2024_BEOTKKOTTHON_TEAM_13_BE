package com.team13.n1.service;



import com.team13.n1.entity.Post;
import com.team13.n1.entity.User;

import java.util.List;

public interface UserLikePostService {
    List<Post> getLikedPostsByUser(User user);
}

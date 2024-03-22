package com.team13.n1.repository;


import com.team13.n1.entity.User;
import com.team13.n1.entity.UserLikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikePostRepository extends JpaRepository<UserLikePost, Integer> {
    List<UserLikePost> findByUser(User user);
}

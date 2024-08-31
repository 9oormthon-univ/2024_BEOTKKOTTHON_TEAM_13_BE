package com.team13.serviceuser.service;

import com.team13.serviceuser.dto.PostResponseDto;
import com.team13.serviceuser.feign.PostServiceClient;
import com.team13.serviceuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {
    
    @Autowired
    private PostServiceClient postServiceClient;

    public List<PostResponseDto> getPostsByUserId(Long userId) {
        return postServiceClient.getPostsByUserId(userId);
    }


}

package com.team13.n1.service;

import com.team13.n1.entity.Review;
import com.team13.n1.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;

    // 새로운 리뷰 등록
    public void save(Map<String, String> request) {
        Review review = new Review();
        review.setFromUserId(request.get("from_user_id"));
        review.setToUserId(request.get("to_user_id"));
        review.setPostId(Integer.parseInt(request.get("post_id")));
        review.setText(request.get("text"));
        review.setScore(request.get("score"));
        repository.save(review);
    }
}

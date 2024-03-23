package com.team13.n1.service;

import com.team13.n1.dto.PostCreateRequest;
import com.team13.n1.dto.PostIngredientDto;
import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.PostIngredientRepository;
import com.team13.n1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// PostCreateService.java
@Service
public class PostCreateService {
    private final PostRepository postRepository;
    private final PostIngredientRepository postIngredientRepository;

    @Autowired
    public PostCreateService(PostRepository postRepository, PostIngredientRepository postIngredientRepository) {
        this.postRepository = postRepository;
        this.postIngredientRepository = postIngredientRepository;
    }

    @Value("${upload.dir}")
    private String uploadDir;

    public void postIngd(PostCreateRequest request) {

        Post post = new Post();
        post.setUserId("abc"); //로그인하면 필요없음
        post.setTitle(request.getTitle());
        post.setPrice(request.getPrice());
        post.setContents(request.getContents());
        post.setImages(request.getImages());
        post.setGroupSize(request.getGroupSize());
        post.setContents(request.getContents());
        post.setClosedAt(new Date());
        post.setType(0); //0==INGD,1==RINGD
        post.setStatus(0); //0 진행중, 1은 완료 혹은 종료
        post.setLocationAddress("부산광역시 사하구");
        post.setLocationBcode("00000011111");
        post.setLocationLatitude("12.123");
        post.setLocationLongitude("12.123");
        post.setChatId("abcchat");
        post.setCreatedAt(new Date());
        post.setCurGroupSize(0);
        post.setUrl(request.getUrl());


        // Post를 저장
        Post savedPost = postRepository.save(post);

        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            // 첫 번째 ingredient만 선택하여 처리
            PostIngredientDto firstIngredient = request.getIngredients().get(0);
            PostIngredient ingredient = convertToEntity(firstIngredient);
            // Post와 연관시킴
            ingredient.setPost(savedPost);
            postIngredientRepository.save(ingredient);
        }
    }

    public void postRIngd( PostCreateRequest request) {
        Post post = new Post();
        post.setUserId("kkk"); //로그인하면 필요없음
        post.setTitle(request.getTitle());
        post.setPrice(request.getPrice());
        post.setContents(request.getContents());
        post.setImages(request.getImages());
        post.setGroupSize(request.getGroupSize());
        post.setContents(request.getContents());
        post.setClosedAt(new Date());
        post.setType(0); //0==INGD,1==RINGD
        post.setStatus(0); //0 진행중, 1은 완료 혹은 종료
        post.setLocationAddress("서울시 성북구");
        post.setLocationBcode("000000133311");
        post.setLocationLatitude("13.222");
        post.setLocationLongitude("13.222");
        post.setChatId("Ringd");
        post.setCreatedAt(new Date());
        post.setCurGroupSize(0);
        post.setUrl(request.getUrl());

        // Post를 저장
        Post savedPost = postRepository.save(post);

        // PostIngredient를 저장하기 전에 Post와 연관시켜야 함
        if (request.getIngredients() != null) {
            for (PostIngredientDto ingredientDto : request.getIngredients()) {
                PostIngredient ingredient = convertToEntity(ingredientDto);
                // Post와 연관시킴
                ingredient.setPost(savedPost);
                postIngredientRepository.save(ingredient);
            }
        }
    }

    private PostIngredient convertToEntity(PostIngredientDto ingredientDto) {
        PostIngredient ingredient = new PostIngredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setUrl(ingredientDto.getUrl());
        return ingredient;
    }

    public String saveImageAndReturnPath(MultipartFile imageFile) {
        String imageName = imageFile.getOriginalFilename();
        String imagePath = uploadDir + "/" + imageName;
        File dest = new File(imagePath);
        try {
            imageFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    }



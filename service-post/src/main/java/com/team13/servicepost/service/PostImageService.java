package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.repository.PostImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;

    @Value("${file.post-upload-dir}")
    private String uploadDir;

    public PostImage saveImageFile(MultipartFile multipartFile, Post post) {
        try {
            String fileName = UUID.randomUUID() + ".png" ;
            String fullPath = uploadDir + fileName;

            // 디렉토리 없으면 생성
            File dest = new File(fullPath);
            dest.getParentFile().mkdirs();

            // 파일 저장
            multipartFile.transferTo(dest);

            // DB에 저장할 경로
            String dbPath = "/images/post/" + fileName;

            // 엔티티 생성 및 저장
            PostImage postImage = PostImage.builder()
                    .post(post)
                    .imagePath(dbPath)
                    .build();

            return postImageRepository.save(postImage);

        } catch (IOException e) {
            throw new RuntimeException("게시글 이미지 저장 실패", e);
        }
    }

    /**
     * 특정 게시글 ID로 이미지 리스트 조회
     */
    public List<PostImage> getImagesByPostId(Long postId) {
        return postImageRepository.findByPostId(postId);
    }

}

package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostDto;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.feign.UserFeignClient;
import com.team13.servicepost.util.RandomPostGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final UserFeignClient userFeignClient; // 서비스 간 통신 테스트용 Feign Client

    @GetMapping
    public String index() {
        return "Index page of service-post";
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public String serviceConnectionTest() {
        return userFeignClient.serviceConnectionTest().data();
    }

    //post 게시물 1개 보기
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {

        int groupSize = RandomPostGenerator.groupSize();
        int curGroupSize = RandomPostGenerator.curGroupSize(groupSize);

        int type = RandomPostGenerator.type();

        Post post = new Post();
        post.setId(id);

        Date createdAt = RandomPostGenerator.createdAt();
        Date closedAt = RandomPostGenerator.closedAt(createdAt);

        PostDto postDto = PostDto.builder()
                .id(id)
                .status(RandomPostGenerator.status())
                .userNickname(RandomPostGenerator.userNickname())
                .groupSize(groupSize)
                .curGroupSize(curGroupSize)
                .chatId(RandomPostGenerator.chatId())
                .createdAt(createdAt)
                .closedAt(closedAt)
                .locationAddress(RandomPostGenerator.locationAddress())
                .locationLongitude(RandomPostGenerator.locationLongitude())
                .locationLatitude(RandomPostGenerator.locationLatitude())
                .title(RandomPostGenerator.title())
                .pricePerUser(RandomPostGenerator.pricePerUser())
                .type(type)
                .contents(RandomPostGenerator.contents())
                .ingredients(RandomPostGenerator.ingredients(type, post))
                .images(RandomPostGenerator.images(post))
                .likesCount(RandomPostGenerator.likesCount())
                .build();

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<PostDto>> list(@RequestParam(value = "bcode", defaultValue = "4113510300") String bCode,
                                              @RequestParam(value = "type", defaultValue = "all") String strType,
                                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                              @RequestParam(value = "page", defaultValue = "1") int pageName) {

        List<PostDto> posts = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            int type = strType.equals("all") ? RandomPostGenerator.type() :
                    strType.equals("ingd") ? 0 :
                            strType.equals("r_ingd") ? 1 : -1;

            if (type == -1) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            int groupSize = RandomPostGenerator.groupSize();
            int curGroupSize = RandomPostGenerator.curGroupSize(groupSize);

            long postId = RandomPostGenerator.id();

            Post post = new Post();
            post.setId(postId);

            posts.add(PostDto.builder()
                    .id(postId)
                    .status(RandomPostGenerator.status())
                    .userNickname(RandomPostGenerator.userNickname())
                    .groupSize(groupSize)
                    .curGroupSize(curGroupSize)
                    .createdAt(RandomPostGenerator.createdAt())
                    .locationLongitude(RandomPostGenerator.locationLongitude())
                    .locationLatitude(RandomPostGenerator.locationLatitude())
                    .title(RandomPostGenerator.title())
                    .pricePerUser(RandomPostGenerator.pricePerUser())
                    .type(type)
                    .ingredients(RandomPostGenerator.ingredients(type, post))
                    .images(RandomPostGenerator.images(post))
                    .build());

        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}

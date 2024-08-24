package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostDto;
import com.team13.servicepost.feign.UserFeignClient;
import com.team13.servicepost.util.RandomPostGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

            posts.add(PostDto.builder()
                            .id(RandomPostGenerator.id())
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
                            .ingredients(RandomPostGenerator.ingredients(type))
                            .images(RandomPostGenerator.images())
                    .build());

        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
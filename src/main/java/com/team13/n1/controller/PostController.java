package com.team13.n1.controller;

import com.team13.n1.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Log4j2
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    String[] ingd_names = {"감자 공동구매 합니다.", "양파 한 묶음 공동구매 합니다~", "고구마 공동구매 합니다!", "대파 공동구매 합니다.", "마늘 공동구매 합니다."};
    String[] recipe_names = {"샐로드 재료 공동구매 합니다.", "카레 재료 공둥구매 진행합니다!", "제육볶음 재료 공동구매 합니다.", "김밥 재료 공동구매!", "오이소박이 요리 재료 공동구매 진행합니다!"};
    String[] ingd = {"카레가루", "당근", "양파", "후추", "계란", "마늘", "오이", "호루라기"};

    @GetMapping("list")
    public List<Map<String, Object>> list(@RequestParam(value = "type") String type,
                                          @RequestParam(value = "bcode", required = false) String bCode,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "page") int page) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Map<String, Object> post = new HashMap<>();

            int id = new Random().nextInt(50);
            post.put("id", Integer.toString(id));
            post.put("type", type);

            if (type.equals("all")) {
                int randomValue = new Random().nextInt(2);
                post.put("title", (randomValue == 0) ? ingd_names[id % 5] : recipe_names[id % 5]);
                if (randomValue == 1)
                    post.put("ingredients", ingd);
                post.put("type", (randomValue == 0) ? "ingd" : "r_ingd");
            } else if (type.equals("ingd")) {
                post.put("title", ingd_names[id % 5]);
            } else if (type.equals("r_ingd")) {
                post.put("title", ingd_names[id % 5]);
                post.put("ingd", ingd);
            }

            post.put("image", "/user-image/post/1.jpg");
            post.put("price", Integer.toString((new Random().nextInt(999) * 10)));

            Map<String, String> user = new HashMap<>();
            user.put("nickname", "윤준영");
            user.put("rating", "89");
            post.put("user", user);

            Map<String, String> location = new HashMap<>();
            location.put("longitude", "36.772158");
            location.put("latiude", "126.932922");
            post.put("location", location);

            result.add(post);
        }

        return result;
    }
}

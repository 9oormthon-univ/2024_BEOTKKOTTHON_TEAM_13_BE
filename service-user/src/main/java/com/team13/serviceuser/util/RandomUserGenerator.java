package com.team13.serviceuser.util;

import com.team13.serviceuser.dto.UserDto;

import java.util.List;
import java.util.Random;

public class RandomUserGenerator {

    private static final Random random = new Random();

    // 임의의 이메일 도메인 목록
    private static final List<String> EMAIL_DOMAINS = List.of(
            "@gamil.com", "@naver.com", "@daum.net", "@kakao.com"
    );

    // 임의의 닉네임을 위한 형용사와 명사 목록
    private static final List<String> ADJECTIVES = List.of(
            "친절한", "사교적인", "성실한", "정직한", "용감한", "활발한", "책임감 있는", "인내심 있는", "겸손한", "유머러스한"
    );

    private static final List<String> NOUNS = List.of(
            "고래", "호랑이", "사자", "토끼", "부엉이", "여우", "늑대", "펭귄", "곰", "도마뱀"
    );

    // 임의의 프로필 이미지 URL 목록
    private static final List<String> PROFILE_IMAGE_URLS = List.of(
            "https://example.com/images/profile1.jpg",
            "https://example.com/images/profile2.jpg",
            "https://example.com/images/profile3.jpg",
            "https://example.com/images/profile4.jpg"
    );

    public static String generateEmailId() {
        int length = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder chatIdBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            chatIdBuilder.append(characters.charAt(index));
        }

        return chatIdBuilder.toString();
    }


    // 임의의 사용자 ID 생성
    public static Long generateId() {
        return (long) (random.nextInt(10) + 1);
    }



    // 임의의 이메일 생성
    public static String generateEmail(String emailId) {
        String domain = EMAIL_DOMAINS.get(random.nextInt(EMAIL_DOMAINS.size()));
        return emailId + domain;
    }

    // 임의의 닉네임 생성
    public static String generateNickname() {
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(random.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }

    // 임의의 사용자 평점 생성
    public static float generateUserRating() {
        return 1.0f + random.nextFloat() * 4.0f; // 1.0 ~ 5.0 사이의 평점
    }

    // 임의의 프로필 이미지 URL 생성
    public static String generateProfileImageUrl() {
        return PROFILE_IMAGE_URLS.get(random.nextInt(PROFILE_IMAGE_URLS.size()));
    }

    // 임의의 UserDto 객체 생성
    public static UserDto generateRandomUser() {
        String nickname = generateNickname();
        return UserDto.builder()
                .id(generateId())
                .email(generateEmail(nickname))
                .nickname(nickname)
                .userRating(generateUserRating())
                .profileImageUrl(generateProfileImageUrl())
                .build();
    }
}

package com.team13.servicerecipe.util;

import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.entity.RecipeProcess;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomRecipeGenerator {

    private static Random random = new Random();

    // 유저 프로파일 이미지 리스트
    private static final List<String> PROFILE_IMAGES = List.of(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    );

    // 형용사 리스트
    private static final List<String> ADJECTIVES = List.of(
            "친절한", "사교적인", "성실한", "정직한", "용감한", "활발한", "책임감 있는", "인내심 있는", "겸손한", "이기적인",
            "게으른", "고집 센", "냉정한", "예민한", "유머러스한", "신중한", "다정한", "호기심 많은", "낙천적인", "비관적인"
    );

    // 명사 리스트
    private static final List<String> NOUNS = List.of(
            "사람", "친구", "학생", "직원", "영웅", "아이", "리더", "부모", "스승", "행동",
            "동료", "성격", "판단", "반응", "대화", "결정", "연인", "아이", "성격", "관점"
    );

    // 제목 리스트
    private static final List<String > TITLES = List.of(
            "비빔밥",
            "갈비찜",
            "잡채",
            "된장찌개",
            "김치찌개",
            "불고기",
            "제육볶음",
            "삼계탕",
            "해물파전",
            "떡볶이",
            "김밥",
            "순두부찌개",
            "냉면",
            "갈비탕",
            "낙지볶음",
            "양념치킨",
            "감자탕",
            "만두",
            "우동",
            "카레라이스"
    );

    // 내용 리스트
    private static final List<String> CONTENTS = List.of(
            "비빔밥은 한국을 대표하는 전통 음식 중 하나로, 다양한 채소와 고기, 그리고 고소한 참기름과 고추장을 함께 비벼 먹는 건강한 한 끼 식사입니다. 재료를 준비하고 밥 위에 올리기만 하면 누구나 쉽게 만들 수 있어요!",
            "갈비찜은 부드럽게 익힌 소갈비를 달콤하면서도 짭짤한 양념에 푹 졸여낸 요리입니다. 손님 접대용으로도 훌륭하며, 시간이 들지만 정성이 가득 담긴 한 그릇으로 식탁을 풍성하게 만들어 줍니다.",
            "잡채는 당면을 주재료로 하여 다양한 채소와 고기를 함께 볶아낸 한국의 전통 반찬입니다. 간단한 양념으로도 맛을 낼 수 있으며, 색감이 아름다워 명절이나 잔치에 빠지지 않는 요리입니다.",
            "된장찌개는 깊고 구수한 맛이 일품인 한국 전통 찌개입니다. 된장과 두부, 야채를 넣고 끓이면 누구나 쉽게 만들 수 있는 집밥의 대표 메뉴입니다.",
            "김치찌개는 숙성된 김치의 깊은 맛과 돼지고기가 어우러진 매콤하고 칼칼한 찌개입니다. 한국인의 밥상에 자주 올라오는 국민 찌개로, 밥 한 그릇 뚝딱 비우게 만드는 매력이 있어요.",
            "불고기는 얇게 저민 쇠고기를 달콤하고 짭짤한 양념에 재워서 구워 먹는 전통 요리입니다. 간편하게 만들 수 있으며, 부드러운 고기와 달콤한 양념의 조화가 일품입니다.",
            "제육볶음은 매콤한 고추장 양념에 돼지고기를 볶아 만든 한국의 대표적인 반찬입니다. 맛깔스러운 매운맛이 밥과 잘 어울리며, 간단하게 만들 수 있는 요리라 바쁜 날에 제격입니다.",
            "삼계탕은 여름철 보양식으로 유명한 요리로, 영양가 가득한 닭 한 마리를 인삼, 대추, 마늘과 함께 푹 고아낸 건강식입니다. 몸이 허할 때 든든한 한 끼로 제격이에요.",
            "해물파전은 파와 해산물을 듬뿍 넣고 부쳐낸 전으로, 바삭하면서도 고소한 맛이 일품입니다. 간단한 재료와 함께 후다닥 만들 수 있어 간식이나 술안주로 인기가 많아요.",
            "떡볶이는 쫀득한 떡과 매콤달콤한 고추장 양념이 어우러진 한국의 대표 길거리 음식입니다. 간단하게 만들 수 있으면서도 중독성 있는 맛으로 남녀노소 모두에게 사랑받고 있어요.",
            "김밥은 김에 밥과 다양한 재료를 넣고 돌돌 말아낸 간편식입니다. 도시락으로도 훌륭하며, 속 재료를 자유롭게 선택할 수 있어 다양하게 즐길 수 있는 요리입니다.",
            "순두부찌개는 부드러운 순두부와 매콤한 양념이 조화롭게 어우러진 찌개입니다. 조리 시간이 짧아 간편하면서도 깊은 맛을 낼 수 있어 바쁜 날에 특히 추천해요.",
            "냉면은 시원한 육수에 얇고 쫄깃한 면발을 말아 먹는 한국의 대표 여름 음식입니다. 더운 여름에 이만한 보양식이 없으며, 간단한 재료로 시원하게 즐길 수 있어요.",
            "갈비탕은 쇠갈비를 푹 끓여낸 맑고 진한 국물 요리로, 깔끔하면서도 깊은 맛이 특징입니다. 간단한 재료로 오랜 시간 끓여내기만 하면 누구나 쉽게 만들 수 있습니다.",
            "낙지볶음은 낙지를 매콤한 양념에 볶아낸 요리로, 쫄깃한 식감과 매운맛이 특징입니다. 밥과 함께 먹으면 중독성이 있어 한 그릇 뚝딱 비울 수 있는 매력적인 반찬입니다.",
            "양념치킨은 바삭하게 튀긴 치킨에 달콤하면서도 매콤한 양념을 버무린 요리입니다. 간단하게 배달 주문해도 좋고, 집에서도 손쉽게 만들어 파티 음식으로 제격입니다.",
            "감자탕은 돼지 등뼈와 감자를 푹 끓여낸 얼큰하고 진한 국물 요리입니다. 다양한 채소와 함께 끓여내면 더욱 깊은 맛을 느낄 수 있으며, 추운 날씨에 따뜻하게 즐기기 좋은 요리입니다.",
            "만두는 얇은 피에 고기와 야채를 넣고 빚어낸 한국의 전통 음식입니다. 찌거나 굽는 방법으로 다양하게 즐길 수 있으며, 간편하게 만들 수 있는 간식이나 식사 대용으로 좋아요.",
            "우동은 두툼한 면발이 특징인 일본식 국수 요리입니다. 간장 베이스의 깔끔한 국물에 다양한 고명을 올려 따뜻하게 즐길 수 있는 한 그릇 요리입니다.",
            "카레라이스는 한국에서 인기 있는 일본식 카레 요리로, 부드러운 카레 소스와 밥이 어우러져 간편하고 든든한 한 끼로 제격입니다. 집에서 손쉽게 만들 수 있어 많은 사랑을 받고 있어요."
    );

    // 썸네일 이미지 리스트
    private static final List<String> THUMBNAILS = List.of(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    );

    // 재료 리스트
    private static final List<String> INGREDIENTS = List.of(
            "고추장",
            "된장",
            "간장",
            "참기름",
            "마늘",
            "양파",
            "깐대파",
            "고춧가루",
            "쌀",
            "김",
            "두부",
            "배추",
            "무",
            "당근",
            "소고기",
            "돼지고기",
            "닭고기",
            "새우젓",
            "미역",
            "고사리"
    );

    // 레시피 단계 리스트
    private static final List<RecipeProcess> PROCESSES = List.of(
            new RecipeProcess(1L, "", "밥을 지어 그릇에 담아 둡니다."),
            new RecipeProcess(2L, "", "고사리, 시금치, 당근, 호박 등의 채소를 각각 데치거나 볶아 준비합니다."),
            new RecipeProcess(3L, "", "달걀을 프라이해서 반숙으로 만듭니다."),
            new RecipeProcess(4L, "", "그릇에 담긴 밥 위에 준비한 채소와 달걀프라이, 그리고 고기(불고기나 다진 쇠고기)를 올립니다."),
            new RecipeProcess(5L, "", "고추장과 참기름을 뿌려 잘 비벼 먹습니다."),
            new RecipeProcess(6L, "", "소갈비를 찬물에 담가 핏물을 빼고, 끓는 물에 살짝 데쳐 불순물을 제거합니다."),
            new RecipeProcess(7L, "", "간장, 설탕, 다진 마늘, 참기름, 후추, 배즙 등을 섞어 양념장을 만듭니다."),
            new RecipeProcess(8L, "", "데친 갈비에 양념장을 넣고 고루 버무린 후, 30분 정도 재워둡니다."),
            new RecipeProcess(9L, "", "재운 갈비를 냄비에 넣고 물을 부어 중약불에서 1시간 정도 끓입니다."),
            new RecipeProcess(10L, "", "당근, 무, 밤 등을 넣고 갈비가 부드럽게 익을 때까지 더 끓입니다."),
            new RecipeProcess(11L, "", "당면을 끓는 물에 6-7분 정도 삶은 후 찬물에 헹궈 물기를 빼줍니다."),
            new RecipeProcess(12L, "", "당근, 양파, 시금치, 버섯 등을 채 썰어 각각 볶아줍니다."),
            new RecipeProcess(13L, "", "삶은 당면에 간장, 설탕, 참기름을 넣고 잘 섞어줍니다."),
            new RecipeProcess(14L, "", "볶아둔 채소와 당면을 함께 넣고 다시 한번 볶아줍니다."),
            new RecipeProcess(15L, "", "깨소금을 뿌려 마무리합니다."),
            new RecipeProcess(16L, "", "냄비에 물을 붓고, 다시마와 멸치를 넣어 10분간 끓여 육수를 만듭니다."),
            new RecipeProcess(17L, "", "육수에서 다시마와 멸치를 건져낸 후 된장을 풀어 넣습니다."),
            new RecipeProcess(18L, "", "감자, 양파, 호박, 두부 등을 먹기 좋게 썰어 넣고 끓입니다."),
            new RecipeProcess(19L, "", "마지막으로 대파와 청양고추를 넣어 5분 더 끓여줍니다."),
            new RecipeProcess(20L, "", "불을 끄고, 참기름을 약간 넣어 마무리합니다.")
    );


    // 레시피 ID 생성
    public static Long id() {
        return (long)(random.nextInt(20) + 1);
    }


    // 유저 프로필 이미지 생성
    public static String userProfileUrl() {
        int randIndex = random.nextInt(20);

        return PROFILE_IMAGES.get(randIndex);
    }


    // 사용자 닉네임 생성
    public static String userNickname() {
        int randIndex1 = random.nextInt(20);
        int randIndex2 = random.nextInt(20);

        return ADJECTIVES.get(randIndex1) + " " + NOUNS.get(randIndex2);
    }


    // 제목 생성
    public static String title() {
        int randIndex = random.nextInt(20);

        return TITLES.get(randIndex);
    }


    // 내용 생성
    public static String contents(int index) {
        return TITLES.get(index);
    }


    // 댓글 개수 생성
    public static int commentCount() {
        int stage = random.nextInt(10);

        if (stage == 0) {
            return random.nextInt(100) + 100;
        } else if (stage < 3) {
            return random.nextInt(50) + 50;
        }
        return random.nextInt(10);
    }


    // 좋아요 개수 생성
    public static int likesCount() {
        int stage = random.nextInt(100);

        if (stage < 1) {
            return random.nextInt(9000) + 1000;
        } else if (stage < 10) {
            return random.nextInt(950) + 50;
        }

        return random.nextInt(50);
    }


    // 썸네일 이미지 생성
    public static String thumbnailImagePath() {
        int randIndex = random.nextInt(20);

        return THUMBNAILS.get(randIndex);
    }


    // 재료 목록 생성
    public static List<RecipeIngredient> ingredients() {
        int randCounter = random.nextInt(7) + 3;

        return IntStream.range(0, randCounter)
                .mapToObj((index) -> {
                    int randIndex = random.nextInt(20);
                    int randAmount = random.nextInt(3) + 1;

                    return new RecipeIngredient((long)randIndex,
                                                INGREDIENTS.get(randIndex),
                                                String.valueOf(randAmount));
                }).toList();
    }


    // 레시피 단계 목록 생성
    public static List<RecipeProcess> processes() {
        int randCounter = random.nextInt(4) + 3;

        return IntStream.range(0, randCounter)
                .mapToObj((index) -> PROCESSES.get(random.nextInt(20))).toList();
    }
}

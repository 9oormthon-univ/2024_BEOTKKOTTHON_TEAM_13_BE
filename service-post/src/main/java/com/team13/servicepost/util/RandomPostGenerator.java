package com.team13.servicepost.util;

import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;

import java.util.*;
import java.util.stream.IntStream;

public class RandomPostGenerator {

    private static Random random = new Random();

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
    private static final List<String> TITLES = List.of(
            "양파 한 묶음 공동구매합니다.",
            "당근 한 상자 함께 구매할 분 구해요.",
            "사과 한 박스 공동구매합니다.",
            "대파 한 단 같이 사요.",
            "고구마 공동구매할 분 찾습니다.",
            "달걀 한 판 같이 사서 나누실 분?",
            "배추 공동구매할 분 있나요?",
            "토마토 박스 같이 구매하실 분 구합니다.",
            "미역 한 묶음 공동구매할 분 있나요?",
            "쌀 10kg 함께 주문하실 분 구해요.",
            "마늘 한 망 나눠서 구매할 분 있나요?",
            "김치찌개 재료 같이 준비할 분 구합니다!",
            "불고기 재료 공동구매할 분 있나요?",
            "된장찌개 재료 함께 구매할 분 찾습니다.",
            "잡채 재료 나눠서 구매하실 분 구해요.",
            "갈비찜 재료 공동구매합니다.",
            "비빔밥 재료 같이 사서 요리할 분 구해요.",
            "제육볶음 재료 묶음으로 같이 주문하실 분?",
            "김밥 재료 함께 구입할 분 구합니다.",
            "해물파전 재료 공동구매할 분 있나요?"
    );

    // 내용 리스트
    private static final List<String> CONTENTS = List.of(
            "신선한 양파 대량으로 구매 예정입니다. 양파는 매일 요리에서 자주 사용하는 재료로, 오래 두고 사용할 수 있어 공동구매에 최적입니다. 10kg 한 묶음에 15,000원으로 매우 경제적인 가격입니다. 함께 구매하실 분들은 사당역에서 직거래 가능하니 편하게 연락 주세요. 양파가 필요한 분들 함께 구매해서 절약해요!",
            "싱싱한 국내산 당근을 한 상자 구매하려고 합니다. 당근은 샐러드, 볶음요리, 스프 등 다양한 요리에 활용할 수 있어 아주 유용한 재료죠. 한 상자에 12,000원으로, 양도 충분해 여러 요리에 사용하기 좋습니다. 직거래는 잠실역에서 가능하니, 당근 필요하신 분들 함께 구매해요. 오래 두고 사용할 수 있어 부담 없으실 거예요.",
            "과즙이 풍부하고 아삭한 사과를 한 박스 구매할 예정입니다. 사과는 건강에도 좋고 간식으로도 훌륭한 선택이죠. 5kg 박스에 20,000원으로, 신선한 사과를 저렴하게 구입할 수 있는 기회입니다. 강남역에서 직거래 가능하니, 함께 구매하실 분들 연락 주시면 좋겠습니다. 가족 모두가 좋아할 사과를 경제적으로 나눠요!",
            "대파는 거의 모든 요리에 빠질 수 없는 재료입니다. 신선한 대파를 한 단 구매하려고 하는데, 함께 구매하실 분을 찾습니다. 한 단에 5,000원이며, 양이 많아 여러 번 요리할 수 있어 아주 유용합니다. 신촌역에서 직거래 가능하니 필요하신 분들 연락 주세요. 대파를 미리 구매해 두면 요리 준비가 훨씬 수월해질 거예요.",
            "달콤하고 맛있는 밤고구마를 5kg 구매할 예정입니다. 고구마는 간식으로도, 다양한 요리 재료로도 아주 좋죠. 5kg에 18,000원으로 아주 합리적인 가격입니다. 직거래는 홍대입구역에서 가능합니다. 함께 구매하셔서 든든한 고구마를 합리적인 가격에 나눠가져요. 오랫동안 두고두고 드실 수 있어요!",
            "신선한 유정란을 한 판 구매할 예정입니다. 달걀은 아침 식사, 베이킹, 요리 등 어디에나 빠질 수 없는 필수 재료죠. 한 판에 7,000원으로 아주 좋은 가격입니다. 역삼역에서 직거래 가능하니, 신선한 달걀 함께 구매하실 분 연락 주세요. 매일 사용하기 좋은 양이라 부담 없이 구매하실 수 있습니다.",
            "무농약 배추를 3포기 묶음으로 구매하려고 합니다. 배추는 김치 담그기, 배추전, 배추국 등 다양한 요리에 활용할 수 있어요. 3포기에 15,000원으로 매우 합리적인 가격입니다. 교대역에서 직거래 가능하니, 배추 필요하신 분들 함께 구매하시면 좋겠습니다. 신선한 배추를 저렴하게 나누어 써요!",
            "신선하고 즙이 많은 방울토마토를 한 박스 구매하려고 합니다. 토마토는 샐러드, 소스, 간식 등 다양하게 활용할 수 있는 식재료입니다. 3kg 박스에 10,000원으로, 아주 경제적인 가격에 구매할 수 있습니다. 을지로입구역에서 직거래 가능하니, 방울토마토를 좋아하시는 분들 함께 구매해요. 건강에도 좋고, 맛도 좋은 토마토를 저렴하게 만나보세요!",
            "자연산 미역을 한 묶음 구매할 예정입니다. 미역은 미역국, 샐러드 등 건강식으로 아주 좋습니다. 한 묶음에 8,000원으로, 양이 많아 여러 번 나누어 사용할 수 있어 경제적입니다. 종각역에서 직거래 가능하니, 신선한 미역 함께 구매하실 분들 연락 주세요. 오랫동안 신선하게 보관하며 드실 수 있습니다.",
            "고품질 햅쌀 10kg을 구매하려고 합니다. 쌀은 우리 식탁에 빠질 수 없는 주식이죠. 10kg에 25,000원으로 품질 대비 가격이 매우 좋습니다. 잠실역에서 직거래 가능하니, 함께 구매하실 분들 연락 주시면 좋겠습니다. 신선한 쌀을 함께 나누어 경제적으로 사용해보세요. 쌀은 두고두고 사용하기 좋은 양입니다.",
            "국내산 마늘을 한 망 구매할 예정입니다. 마늘은 모든 요리에 풍미를 더해주는 필수 재료죠. 한 망에 12,000원으로, 양도 충분해 오래 사용하실 수 있습니다. 서초역에서 직거래 가능하니, 마늘 필요하신 분들 함께 구매하시면 좋겠습니다. 신선한 마늘을 합리적인 가격에 나누어 써요!",
            "김치찌개용 돼지고기와 채소를 함께 구매할 분을 찾습니다. 김치찌개는 한국인의 소울푸드죠. 돼지고기와 각종 채소를 포함한 세트가 20,000원으로 매우 저렴합니다. 강남역에서 직거래 가능하니, 함께 김치찌개 재료를 준비해 맛있는 한 끼를 만들어보아요. 같이 구매하면 더 저렴하고, 푸짐하게 즐길 수 있습니다.",
            "불고기용 소고기를 함께 구매할 분을 찾습니다. 불고기는 남녀노소 누구나 좋아하는 인기 메뉴죠. 1kg에 28,000원으로 신선한 고기를 합리적인 가격에 구매할 수 있습니다. 신사역에서 직거래 가능하니, 맛있는 불고기를 함께 준비해볼까요? 함께 구매하면 더 저렴하게 신선한 고기를 구할 수 있습니다.",
            "된장찌개용 된장, 두부, 채소 등 재료를 함께 구매할 분을 구합니다. 된장찌개는 깊은 맛을 내는 한국 전통 요리죠. 전체 세트가 15,000원에 제공되며, 신선한 재료들로 가득합니다. 건대입구역에서 직거래 가능하니, 함께 맛있는 된장찌개를 준비해보세요. 저렴한 가격에 여러 재료를 함께 구매하면 더 알뜰하게 요리할 수 있습니다.",
            "잡채용 채소와 고기 세트를 구매할 분을 찾습니다. 잡채는 행사나 특별한 날에 빠질 수 없는 인기 요리죠. 20,000원에 신선한 재료를 함께 나누어 구매할 수 있습니다. 왕십리역에서 직거래 가능하니, 잡채 재료가 필요하신 분들 연락 주세요. 함께 구매하면 더 저렴하게 여러 재료를 구할 수 있어요!",
            "맛있는 갈비찜 재료를 함께 구매할 분을 찾습니다. 갈비찜은 특별한 날이나 명절에 빠질 수 없는 인기 메뉴입니다. 갈비와 각종 채소, 양념을 포함한 재료 세트를 30,000원에 준비할 예정입니다. 신선한 재료로 푸짐하게 준비할 수 있어요. 사당역에서 직거래 가능하니, 갈비찜 준비하실 분들 함께 구매해 저렴하게 맛있는 갈비찜을 만들어보아요!",
            "영양 가득한 비빔밥 재료를 함께 구매할 분을 찾습니다. 비빔밥은 다양한 채소와 고기, 양념장이 어우러져 누구나 좋아하는 음식이죠. 12,000원에 신선한 재료들을 함께 구매할 예정입니다. 홍대입구역에서 직거래 가능하니, 비빔밥 재료가 필요하신 분들 함께 구매해요. 여러 가지 재료를 저렴하게 준비해 맛있게 비벼 드세요!",
            "제육볶음용 돼지고기와 양념을 함께 구매할 분을 찾습니다. 제육볶음은 매콤하고 맛있어 많은 분들이 좋아하는 메뉴입니다. 18,000원에 양념이 잘 배인 돼지고기를 포함한 재료를 묶음으로 준비할 예정입니다. 신촌역에서 직거래 가능하니, 맛있는 제육볶음을 함께 만들어 드실 분들 연락 주세요. 같이 구매하면 더 저렴하고 푸짐하게 즐길 수 있어요!",
            "김밥 재료를 함께 구매하실 분을 찾습니다. 김밥은 간편하게 먹기 좋고, 피크닉이나 소풍에 빠질 수 없는 음식이죠. 15,000원에 다양한 재료들을 포함한 묶음을 구매할 예정입니다. 강남역에서 직거래 가능하니, 김밥을 함께 만들 분들 연락 주세요. 저렴하게 신선한 재료를 준비해 김밥을 맛있게 만들어보세요!",
            "바삭하고 고소한 해물파전 재료를 함께 구매할 분을 찾습니다. 해물파전은 비 오는 날이나 간식으로 딱 좋은 메뉴죠. 해산물과 파를 포함한 재료 세트를 18,000원에 준비할 예정입니다. 종로3가역에서 직거래 가능하니, 함께 해물파전 재료를 구매해 저렴하게 맛있는 파전을 만들어보아요. 신선한 재료로 가족과 함께 즐기기 좋아요!"
    );

    // 재료 리스트
    private static final List<PostIngredient> INGREDIENTS = List.of(
            new PostIngredient(1L, "고추장", "https://www.kurly.com/goods/5156563"),
            new PostIngredient(2L, "된장", "https://www.coupang.com/vp/products/7235480382?itemId=13507099803&vendorItemId=3000138235&pickType=COU_PICK&q=%EB%90%9C%EC%9E%A5&itemsCount=27&searchId=fd5785e3e3cc4bf5bd9564acaefc85d1&rank=0&isAddedCart="),
            new PostIngredient(3L, "간장", "https://www.kurly.com/goods/5156583"),
            new PostIngredient(4L, "참기름", "https://www.coupang.com/vp/products/7235432441?itemId=3019599468&vendorItemId=84993756491&pickType=COU_PICK&q=%EC%B0%B8%EA%B8%B0%EB%A6%84&itemsCount=27&searchId=e16ef9ad8dbd45e3b24539697e3d7080&rank=0&isAddedCart="),
            new PostIngredient(5L, "마늘", "https://www.kurly.com/goods/5027318"),
            new PostIngredient(6L, "양파", "https://www.coupang.com/vp/products/1074470755?itemId=2573048645&vendorItemId=70565380605&pickType=COU_PICK&q=%EC%96%91%ED%8C%8C&itemsCount=27&searchId=258a059f4f854434ad9ff92317d20c74&rank=0&isAddedCart="),
            new PostIngredient(7L, "깐대파", "https://www.kurly.com/goods/5027320"),
            new PostIngredient(8L, "고춧가루", "https://www.coupang.com/vp/products/188717799?itemId=2089138674&vendorItemId=70088115437&pickType=COU_PICK&q=%EA%B3%A0%EC%B6%A7%EA%B0%80%EB%A3%A8&itemsCount=27&searchId=b4d36fa793be43de8cef583ee0c4f36f&rank=0&isAddedCart="),
            new PostIngredient(9L, "쌀", "https://www.kurly.com/goods/5003169"),
            new PostIngredient(10L, "김", "https://www.coupang.com/vp/products/266254917?itemId=834542714&vendorItemId=5124578637&q=%EA%B9%80&itemsCount=27&searchId=9141b44bfa3c4496b6dd27db4ac5a234&rank=0&isAddedCart="),
            new PostIngredient(11L, "두부", "https://www.kurly.com/goods/5053329"),
            new PostIngredient(12L, "배추", "https://www.coupang.com/vp/products/2233944989?itemId=3812413037&vendorItemId=71797311591&pickType=COU_PICK&q=%EB%B0%B0%EC%B6%94&itemsCount=27&searchId=ea435bc585b34aa8a5eb4e52280af94f&rank=0&isAddedCart="),
            new PostIngredient(13L, "무", "https://www.kurly.com/goods/5049635"),
            new PostIngredient(14L, "당근", "https://www.coupang.com/vp/products/6202345562?itemId=12314074324&vendorItemId=79584195630&q=%EB%8B%B9%EA%B7%BC&itemsCount=27&searchId=6a8aec6bf3f24b759099fd5c756a53d9&rank=1&isAddedCart="),
            new PostIngredient(15L, "소고기", "https://www.kurly.com/goods/5054443"),
            new PostIngredient(16L, "돼지고기", "https://www.coupang.com/vp/products/5923718611?itemId=10507163351&vendorItemId=77788822678&q=%EB%8F%BC%EC%A7%80%EA%B3%A0%EA%B8%B0&itemsCount=26&searchId=226d843ffa48495eaf9104870742ebbb&rank=2&isAddedCart="),
            new PostIngredient(17L, "닭고기", "https://www.kurly.com/goods/5030115"),
            new PostIngredient(18L, "새우젓", "https://www.coupang.com/vp/products/6957124407?itemId=16922960532&vendorItemId=84100794087&pickType=COU_PICK&q=%EC%83%88%EC%9A%B0%EC%A0%93&itemsCount=27&searchId=365f83c088424850bfb0c9ed03ff19a7&rank=0&isAddedCart="),
            new PostIngredient(19L, "미역", "https://www.kurly.com/goods/5004778"),
            new PostIngredient(20L, "고사리", "https://www.coupang.com/vp/products/6342738185?itemId=13313872019&vendorItemId=80570095547&pickType=COU_PICK&q=%EA%B3%A0%EC%82%AC%EB%A6%AC&itemsCount=27&searchId=7f77adf0ea4c4e2eacc63050bda5d5f2&rank=0&isAddedCart=")
    );

    // 이미지 리스트
    private static final List<PostImage> IMAGES = List.of(
            new PostImage(1L, ""),
            new PostImage(2L, ""),
            new PostImage(3L, ""),
            new PostImage(4L, ""),
            new PostImage(5L, ""),
            new PostImage(6L, ""),
            new PostImage(7L, ""),
            new PostImage(8L, ""),
            new PostImage(9L, ""),
            new PostImage(10L, ""),
            new PostImage(11L, ""),
            new PostImage(12L, ""),
            new PostImage(13L, ""),
            new PostImage(14L, ""),
            new PostImage(15L, ""),
            new PostImage(16L, ""),
            new PostImage(17L, ""),
            new PostImage(18L, ""),
            new PostImage(19L, ""),
            new PostImage(20L, "")
    );


    // 게시글 ID 생성
    public static Long id() {
        return (long)(random.nextInt(20) + 1);
    }


    // 게시글 상태 생성
    public static int status() {
        return 1;
    }


    // 사용자 닉네임 생성
    public static String userNickname() {
        int randIndex1 = random.nextInt(20);
        int randIndex2 = random.nextInt(20);

        return ADJECTIVES.get(randIndex1) + " " + NOUNS.get(randIndex2);
    }


    // 총 참여자 수 생성
    public static int groupSize() {
        return random.nextInt(4) + 2;
    }


    // 현재 참여자 수 생성
    public static int curGroupSize(int groupSize) {
        return random.nextInt(groupSize + 1);
    }


    // 작성일 생성
    public static Date createdAt() {
        Calendar calendar = new GregorianCalendar();

        int randDay = random.nextInt(30);
        calendar.add(Calendar.DATE, randDay * -1);

        return Date.from(calendar.toInstant());
    }


    // 경도 생성
    public static String locationLongitude() {
        double baseLongitude = 127.112852;

        return Double.toString(baseLongitude + ((random.nextDouble() - 0.5) * 0.01));
    }


    // 위도 생성
    public static String locationLatitude() {
        double baseLatitude = 37.366326;

        return Double.toString(baseLatitude + ((random.nextDouble() - 0.5) * 0.01));
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


    // 인당 가격 생성
    public static int pricePerUser() {
        int rand = random.nextInt(200) + 10;

        return rand * 100;
    }


    // 게시물 타입 생성
    public static int type() {
        return random.nextInt(2);
    }


    // 재료 목록 생성
    // type이 0이면 재료 하나 반환, 1이면 여러 개의 재료 반환
    public static List<PostIngredient> ingredients(int type) {
        if (type == 0) {

            int randIndex = random.nextInt(20);

            return List.of(INGREDIENTS.get(randIndex));

        } else {

            int randCounter = random.nextInt(5) + 3;

            return IntStream.range(0, randCounter)
                    .mapToObj((index) -> INGREDIENTS.get(random.nextInt(20))).toList();

        }
    }


    // 이미지 목록 생성
    public static List<PostImage> images() {
        int randCounter = random.nextInt(4) + 1;

        return IntStream.range(0, randCounter)
                .mapToObj((index) -> IMAGES.get(random.nextInt(20))).toList();
    }
}

package com.example.ArtAuction_24.global.base.initData;


import com.example.ArtAuction_24.domain.answer.service.AnswerService;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DevInitData implements BeforeInitData {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ProductService productService;
    private final ArtistService artistService;
    private final QuestionService questionService;
    private final AnswerService answerService;



    @Bean
    CommandLineRunner init(MemberService memberService, ArtistService artistService, ProductService productService) {

        return args -> {
            beforeInit();
            String password = "test123!";

            memberService.join("", "admin", password, "admin@test.com", "admin",
                    "010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

            Member M1 = memberService.join("", "user1", password, "user1@test.com", "user1",
                    "010-1234-5678", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

            memberService.join("", "user2", password, "user2@test.com", "user2",
                    "010-1314-5838", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

            memberService.join("", "artist", password, "artist1@test.com", "artist1",
                    "010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");


            artistService.create("김작가", "kimArtist", "1950-06-28", "010-1234-5678","Artist@naver.com","naver.com","안녕! 나 김작가", "SBS 아트협회 임원");
            artistService.create("나작가", "naArtist", "1968-12-08", "010-4567-5678","Artist@google.com","google.com","안녕! 나 나작가", "SBS 아트협회 임원");
            artistService.create("박작가", "parkArtist", "1999-04-03", "010-9874-5678","Artist@daum.com","daum.com","안녕! 나 박작가", "SBS 아트협회 임원");
            artistService.create("이작가", "leeArtist", "1985-11-02", "010-0582-5678","Artist@kakao.com","kakao.com","안녕! 나 이작가", "SBS 아트협회 임원");
            artistService.create("최작가", "choiArtist", "1975-01-15", "010-8765-1234", "Artist@outlook.com", "outlook.com", "안녕! 나 최작가", "SBS 아트협회 임원");
            artistService.create("정작가", "jungArtist", "1980-07-19", "010-2345-6789", "Artist@yahoo.com", "yahoo.com", "안녕! 나 정작가", "SBS 아트협회 임원");
            artistService.create("강작가", "kangArtist", "1992-03-25", "010-3456-7890", "Artist@msn.com", "msn.com", "안녕! 나 강작가", "SBS 아트협회 임원");
            artistService.create("한작가", "hanArtist", "2001-09-17", "010-5678-0123", "Artist@hotmail.com", "hotmail.com", "안녕! 나 한작가", "SBS 아트협회 임원");

            Artist kim = artistService.getArtistByKorName("김작가");
            Artist na = artistService.getArtistByKorName("나작가");
            Artist park = artistService.getArtistByKorName("박작가");
            Artist lee = artistService.getArtistByKorName("이작가");

            productService.create("토끼 그림", "토끼를 그린 그림입니다.", "사용된 재료", "10x10 크기", new BigDecimal(10000), new BigDecimal(10000), LocalDateTime.now(), "/image/토끼.jpg", "수채화", kim);
            productService.create("은하계 그림", "은하계를 그린 그림입니다.", "사용된 재료", "10x10 크기", new BigDecimal(11000), new BigDecimal(11000), LocalDateTime.now(), "/image/풀사진.jpg", "수채화", na);
            productService.create("영혼 그림", "영혼을 그린 그림입니다.", "사용된 재료", "10x10 크기", new BigDecimal(12000), new BigDecimal(12000), LocalDateTime.now(), "/image/아이사진.jpg", "수채화", park);
            productService.create("산 그림", "산 그림 설명", "사용된 재료", "10x10 크기", new BigDecimal(13000), new BigDecimal(13000), LocalDateTime.now(), "/image/산사진.jpg", "수채화", lee);
            productService.create("노란머리여자 그림", "노란머리여자 그림 설명", "사용된 재료", "10x10 크기", new BigDecimal(14000), new BigDecimal(14000), LocalDateTime.now(), "/image/노란머리여자사진.jpg", "수채화", kim);
            productService.create("그리다만여자 그림", "그리다만여자 그림 설명", "사용된 재료", "10x10 크기", new BigDecimal(15000), new BigDecimal(15000), LocalDateTime.now(), "/image/그리다만여자사진.jpg", "수채화", na);
            productService.create("눈 그림", "눈 그림 설명", "사용된 재료", "10x10 크기", new BigDecimal(60000), new BigDecimal(60000), LocalDateTime.now(), "/image/눈사진.jpg", "수채화", park);
            productService.create("레몬 그림", "레몬 그림 설명", "사용된 재료", "10x10 크기", new BigDecimal(100000), new BigDecimal(100000), LocalDateTime.now(), "/image/레몬사진.jpg", "수채화", lee);



                    //QNA 테스트 데이터
            answerService.create(questionService.create(   new QuestionForm(  "작가의 주된 작품 스타일은 무엇인가요?", "content", QuestionType.ARTIST), M1),  "작가는 주로 추상 미술과 표현주의 스타일을 사용하며, 감정과 내면의 상태를 형상화하는 데 집중합니다", M1);
            answerService.create(questionService.create(   new QuestionForm(  "미술품 경매를 할수있는 작가의 기준은무엇인가요?", "content", QuestionType.ARTIST), M1),  "저희 사이트에서 작가 기준은 예술활동 증명서를 바탕으로 검토를하여 작가자격심사가 이루어집니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "한정판에디션은 무엇인가요?", "content", QuestionType.ARTWORK), M1),  "작가의 한정된 수로 제작하는 작품’을 뜻하며, 작가가 결정합니다. 수량이적을수록 시간이흐를수록 값어치는 점점높아집니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "미술품 판매기준이무엇인가요?", "content", QuestionType.ARTWORK), M1),  "미술품 판매기준은 따로 지정해놓지않았지만 작가가 자신의미술품을 올리는것이며 사람마다 기준이달라 미술품판매기준은 작가에게 있습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "구매는 어떻게하나요?", "content", QuestionType.PURCHASE), M1),  "작가가 미술품을판매하면 경매날짜가 정해집니다 지정날짜가되면 경매가시작되고 입찰을통해 경매마감시 제일높은입찰자에게 판매가됩니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "입찰 방법을알고싶습니다.", "content", QuestionType.PURCHASE), M1),  "충전을하여 입찰캐시를 얻게됩니다 경매물품에 원하는만큼 입찰을하게되면 소유한 캐시에서 차감이되며 더높은입찰자가생겼을때 입찰한금액을 반환받게됩니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "입찰취소방법", "content", QuestionType.PURCHASE), M1),  "입찰하면 다른누군가 더높은금액을 받기전까진 취소할수없습니다. 그점은 주의부탁드리겠습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "사이트에서 제공하는 예술 작품에 대한 상담 서비스는 어떤 것이 있나요?", "content", QuestionType.SERVICE), M1),  "저희 사이트는 전문가 상담 서비스를 제공하여, 작품에 대한 정보와 추천을 받을 수 있습니다. 상담을 통해 작품의 가치와 적합성에 대해 도움을 드립니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "미술품 보관 및 관리 서비스는 어떻게 제공받나요?", "content", QuestionType.SERVICE), M1),  "저희는 전체적인미술품 보관 및 관리 서비스를 제공하고있지않지만, 작가혹은 고객상담을통해 보관 및 관리서비스도 진행하고있습니다. 최적의 환경에서 안전하게 보관합니다. 정기적인 관리와 점검이 포함되어 있습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "간단하게질문 답을 빨리받고싶습니다.", "content", QuestionType.SERVICE), M1),  "간단한 질문같은경우는 오른쪽하단 채팅버튼으로 문의주실수있습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "배송시 액자는 어떻게되는거죠", "content", QuestionType.DELIVERY), M1),  "경매,입찰까지 끝나면 배송이되는데 배송관련은 액자까지해서 같이 배송됩니다 구매자께서 액자준비는 안하셔도됩니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "배송비는 어떻게 책정되나요?", "content", QuestionType.DELIVERY), M1),  "배송비는 작품의 크기와 무게, 배송 지역에 따라 다르게 책정됩니다. 결제 단계에서 정확한 배송비를 확인하실 수 있습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "배송 시 작품이 손상되지 않도록 어떻게 보호하나요?", "content", QuestionType.DELIVERY), M1),  "작품은 전문 포장 재료를 사용하여 포장되며, 충격 완화재와 보호재를 통해 안전하게 배송됩니다. 포장 과정에서 품질 검사를 실시하여 손상을 최소화합니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "작품 반품 절차는 어떻게 되나요?", "content", QuestionType.RETURN), M1),  "작품을 반품하려면, 수령 후 7일 이내에 반품 요청을 하셔야 합니다. 작품이 원 상태로 반송되어야 하며, 반품 비용은 고객님 부담입니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "반품 시 작품이 손상된 경우 어떻게 처리하나요?", "content", QuestionType.RETURN), M1),  "반품된 작품이 손상된 경우, 손상 정도에 따라 환불이 불가능할 수 있습니다. 손상된 부분에 대해 검토 후 안내 드리겠습니다.", M1);
            answerService.create(questionService.create(   new QuestionForm(  "배송받았는데 작품이 파손되어있습니다.", "content", QuestionType.RETURN), M1),  "배송완료후 작품이파손된경우 바로문의주시면 환불을도와드립니다.", M1);
        };
    }

}
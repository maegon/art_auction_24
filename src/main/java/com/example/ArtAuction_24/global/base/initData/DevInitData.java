package com.example.ArtAuction_24.global.base.initData;


import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DevInitData implements BeforeInitData {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuctionProductService auctionProductService;
    private final ArtistService artistService;
    private final AuctionService auctionService;

    @Bean
    CommandLineRunner init(MemberService memberService, ArtistService artistService) {

        return args -> {
            beforeInit();

            String password = "test123!";


            memberService.join("", "admin", password, "admin@test.com", "admin",
                   "010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", "/image/고라파덕.jpg");

            artistService.create("김작가", "kimArtist", "1950-06-28", "010-1234-5678","Artist@naver.com","naver.com","안녕! 나 김작가");
            artistService.create("나작가", "naArtist", "1968-12-08", "010-4567-5678","Artist@google.com","google.com","안녕! 나 나작가");
            artistService.create("박작가", "parkArtist", "1999-04-03", "010-9874-5678","Artist@daum.com","daum.com","안녕! 나 박작가");
            artistService.create("이작가", "leeArtist", "1985-11-02", "010-0582-5678","Artist@kakao.com","kakao.com","안녕! 나 이작가");
            artistService.create("최작가", "choiArtist", "1975-01-15", "010-8765-1234", "Artist@outlook.com", "outlook.com", "안녕! 나 최작가");
            artistService.create("정작가", "jungArtist", "1980-07-19", "010-2345-6789", "Artist@yahoo.com", "yahoo.com", "안녕! 나 정작가");
            artistService.create("강작가", "kangArtist", "1992-03-25", "010-3456-7890", "Artist@msn.com", "msn.com", "안녕! 나 강작가");
            artistService.create("한작가", "hanArtist", "2001-09-17", "010-5678-0123", "Artist@hotmail.com", "hotmail.com", "안녕! 나 한작가");

        };
    }
}

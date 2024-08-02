package com.example.ArtAuction_24.global.base.initData;


import com.example.ArtAuction_24.domain.artist.entity.Artist;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DevInitData implements BeforeInitData {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuctionProductService auctionProductService;
    private final ArtistService artistService;

    @Bean
    CommandLineRunner init(MemberService memberService, ArtistService artistService, AuctionProductService auctionProductService) {

        return args -> {
            beforeInit();

            String password = "test123!";


            memberService.join("", "admin", password, "admin@test.com", "admin",
                   "010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", "/image/고라파덕.jpg");

            memberService.join("", "user1", password, "user1@test.com", "user1",
                    "010-1234-5678", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", "/image/고라파덕.jpg");

            memberService.join("", "user2", password, "user2@test.com", "user2",
                    "010-1314-5838", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", "");

            artistService.create("김작가", "kimArtist", "1950-06-28", "010-1234-5678","Artist@naver.com","naver.com","안녕! 나 김작가");
            artistService.create("나작가", "naArtist", "1968-12-08", "010-4567-5678","Artist@google.com","google.com","안녕! 나 나작가");
            artistService.create("박작가", "parkArtist", "1999-04-03", "010-9874-5678","Artist@daum.com","daum.com","안녕! 나 박작가");
            artistService.create("이작가", "leeArtist", "1985-11-02", "010-0582-5678","Artist@kakao.com","kakao.com","안녕! 나 이작가");
            artistService.create("최작가", "choiArtist", "1975-01-15", "010-8765-1234", "Artist@outlook.com", "outlook.com", "안녕! 나 최작가");
            artistService.create("정작가", "jungArtist", "1980-07-19", "010-2345-6789", "Artist@yahoo.com", "yahoo.com", "안녕! 나 정작가");
            artistService.create("강작가", "kangArtist", "1992-03-25", "010-3456-7890", "Artist@msn.com", "msn.com", "안녕! 나 강작가");
            artistService.create("한작가", "hanArtist", "2001-09-17", "010-5678-0123", "Artist@hotmail.com", "hotmail.com", "안녕! 나 한작가");


            Artist kim = artistService.getArtistByKorName("김작가");
            Artist na = artistService.getArtistByKorName("나작가");
            Artist park = artistService.getArtistByKorName("박작가");
            Artist lee = artistService.getArtistByKorName("이작가");

            auctionProductService.create("파란물고기 그림", "파란물고기 그림 설명", "사용된 재료", "10x10 크기", 10000, 10000, LocalDateTime.now(), "/image/파란물고기사진.jpg", "수채화", kim);
            auctionProductService.create("풀 그림", "풀 그림 설명", "사용된 재료", "10x10 크기", 11000, 11000, LocalDateTime.now(), "/image/풀사진.jpg", "수채화", na);
            auctionProductService.create("아이 그림", "아이 그림 설명", "사용된 재료", "10x10 크기", 12000, 12000, LocalDateTime.now(), "/image/아이사진.jpg", "수채화", park);
            auctionProductService.create("산 그림", "산 그림 설명", "사용된 재료", "10x10 크기", 13000, 13000, LocalDateTime.now(), "/image/산사진.jpg", "수채화", lee);
            auctionProductService.create("노란머리여자 그림", "노란머리여자 그림 설명", "사용된 재료", "10x10 크기", 14000, 14000, LocalDateTime.now(), "/image/노란머리여자사진.jpg", "수채화", kim);
            auctionProductService.create("그리다만여자 그림", "그리다만여자 그림 설명", "사용된 재료", "10x10 크기", 15000, 15000, LocalDateTime.now(), "/image/그리다만여자사진.jpg", "수채화", na);
            auctionProductService.create("눈 그림", "눈 그림 설명", "사용된 재료", "10x10 크기", 60000, 60000, LocalDateTime.now(), "/image/눈사진.jpg", "수채화", park);
            auctionProductService.create("레몬 그림", "레몬 그림 설명", "사용된 재료", "10x10 크기", 100000, 100000, LocalDateTime.now(), "/image/레몬사진.jpg", "수채화", lee);

        };
    }


}

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

            String thumbnailImg = "freedcs/" + "cf053209-e7d4-4167-a28e-5366090fed61" + ".jpg";

            memberService.join("", "admin", password, "admin@test.com", "admin",
                   "010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", "/image/고라파덕.jpg");


        };
    }
}

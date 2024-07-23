package com.example.ArtAuction_24.base.initData;

import com.example.ArtAuction_24.member.entity.Member;
import com.example.ArtAuction_24.member.repository.MemberRepository;
import com.example.ArtAuction_24.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;

@Configuration
@Profile("dev")
public class DevInitData implements BeforeInitData {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;


    @Bean
    CommandLineRunner init(MemberService memberService) {

        return args -> {
            beforeInit();

            String password = "test1234!T";

            memberService.join("admin", password, "admin@test.com", "admin",
                    "010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층", new File("/image/고라파덕.jpg"));

        };
    }
}

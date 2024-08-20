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
             };
    }

}
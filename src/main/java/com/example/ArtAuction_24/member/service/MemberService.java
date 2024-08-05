package com.example.ArtAuction_24.member.service;

import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 
    public Member join(String username, String password, String email, String nickname,
                       String phoneNumber, String address, String imageFileName) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(address)
                .image(imageFileName) // 이미지 파일명 저장(프로필 사진)
                .createDate(LocalDateTime.now())
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return opMember.get();

        return join(username, "", nickname, "", "", "", null);
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

//    public Member getCurrentMember() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return memberRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
//    }

    public Member getCurrentMember() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();

        if (username == null || username.isEmpty()) {
            throw new RuntimeException("로그인 사용자 이름을 찾을 수 없습니다.");
        }

        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + username));
    }

}

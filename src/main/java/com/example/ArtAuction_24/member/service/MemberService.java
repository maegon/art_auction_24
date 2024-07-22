package com.example.ArtAuction_24.member.service;

import com.example.ArtAuction_24.member.entity.Member;
import com.example.ArtAuction_24.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 
    public Member join(String username, String password, String email, String nickname,
                       String phoneNumber, String address, String image) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(address)
                .image(image) // 이미지 파일명 저장(프로필 사진)
                .createDate(LocalDateTime.now())
                .build();
        return memberRepository.save(member);
    }

    // 회원의 username 찾기
    public Member findByUsername(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
    }
}

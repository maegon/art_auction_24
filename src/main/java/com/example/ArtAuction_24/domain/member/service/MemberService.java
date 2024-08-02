package com.example.ArtAuction_24.domain.member.service;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.form.MemberForm;
import com.example.ArtAuction_24.domain.member.form.MemberForm2;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public Member join(String providerTypeCode, String username, String password, String email, String nickname,
                       String phoneNumber, String address, String imageFileName) {
        Member member = Member.builder()
                .providerTypeCode(providerTypeCode)
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
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname, String profileImageUrl, String email) {
        Optional<Member> opMember = findByUsernameAndProviderTypeCode(username, providerTypeCode);

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return join(providerTypeCode, username, "", email,nickname, "", "", profileImageUrl); // 최초 로그인 시 딱 한번 실행
    }

    public Optional<Member> findByUsernameAndProviderTypeCode(String username, String providerTypeCode) {
        return memberRepository.findByUsernameAndProviderTypeCode(username, providerTypeCode);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getCurrentMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }

    public void updateMember(MemberForm2 memberForm, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        member.setUsername(memberForm.getUsername());
        member.setEmail(memberForm.getEmail());
        member.setNickname(memberForm.getNickname());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
    }



    public Member getMember(Long id) {
        Optional<Member> op = memberRepository.findById(id);
        if (op.isPresent() == false) {
            throw new DateTimeException("Member not found");
        }
        return op.get();
    }
}

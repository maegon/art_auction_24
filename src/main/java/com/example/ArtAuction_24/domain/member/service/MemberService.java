package com.example.ArtAuction_24.domain.member.service;

import com.example.ArtAuction_24.domain.member.dto.MemberUpdateRequest;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.form.MemberAddressForm;
import com.example.ArtAuction_24.domain.member.form.MemberModifyForm;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.global.customException.CustomDuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    // 회원가입
    public Member join(String providerTypeCode, String username, String password, String email, String nickname,
                       String phoneNumber, String address) {

        // 이메일 중복 확인
        if (email != null && memberRepository.findByEmail(email).isPresent()) {
            throw new CustomDuplicateEmailException("Email already exists");
        }

        System.out.println(email);

        // 회원 역할 결정
        MemberRole role = "admin".equals(username) ? MemberRole.ADMIN : MemberRole.MEMBER;

        // 회원 객체 생성 및 설정
        Member member = Member.builder()
                .providerTypeCode(providerTypeCode)
                .username(username)
                .password(passwordEncoder.encode(password)) // 비밀번호 암호화
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(address)
                .role(role)
                .isActive(true) // isActive 필드를 true로 설정 (민섭 추가)
                .balance(0L) // 초기화된 balance 값 설정 (민섭추가)
                .createDate(LocalDateTime.now()) // 생성일자
                .artist(null)
                .build();

        return memberRepository.save(member); // 데이터베이스에 저장
    }


    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname, String email) {
        Optional<Member> opMember = findByUsernameAndProviderTypeCode(username, providerTypeCode);

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return join(providerTypeCode, username, "", email, nickname, "", ""); // 최초 로그인 시 딱 한번 실행
    }


    public Optional<Member> findByUsernameAndProviderTypeCode(String username, String providerTypeCode) {
        return memberRepository.findByUsernameAndProviderTypeCode(username, providerTypeCode);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public boolean authenticate(String username, String password) {
        Optional<Member> memberOptional = findByUsername(username);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (passwordEncoder.matches(password, member.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public Member getCurrentMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }

    public void updateMember(MemberModifyForm memberForm, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));



        if (memberForm.getEmail() != null && !memberForm.getEmail().trim().isEmpty()) {
            member.setEmail(memberForm.getEmail());
        }
        if (memberForm.getNickname() != null && !memberForm.getNickname().trim().isEmpty()) {
            member.setNickname(memberForm.getNickname());
        }
        if (memberForm.getPassword() != null && !memberForm.getPassword().trim().isEmpty()) {
            member.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        }

        if (memberForm.getMultipartFile() != null && !memberForm.getMultipartFile().isEmpty()) {
            String thumbnailRelPath = "image/member/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

            thumbnailFile.mkdir();

            try {
                memberForm.getMultipartFile().transferTo(thumbnailFile);
            } catch( IOException e) {
                throw new RuntimeException(e);
            }

            member.setImage(thumbnailRelPath);
        }

        memberRepository.save(member);
    }

    public void updateMemberAddress(MemberAddressForm memberAddressForm, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        if (memberAddressForm.getAddress() != null && !memberAddressForm.getAddress().trim().isEmpty()) {
            member.setAddress(memberAddressForm.getAddress());
        }
        memberRepository.save(member);
    }

    // 아이디 중복 확인
    public boolean checkUsernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }

    // 닉네임 중복 확인
    public boolean checkNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public Long getMemberBalance(Long id) { //민섭 추가
        Member member = getMember(id);
        return member.getBalance();
    }

    public Member getMember(Long id) {
        Optional<Member> op = memberRepository.findById(id);
        if (op.isPresent() == false) {
            throw new DateTimeException("회원을 찾을 수 없습니다.");
        }
        return op.get();
    }
    public Member getMember(String username) {
        Optional<Member> op = memberRepository.findByUsername(username);
        if (op.isPresent() == false) {
            throw new DateTimeException("Member not found");
        }
        return op.get();
    }


    public void updateBalance(String username, Long amount) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        member.setBalance(member.getBalance() + amount);
        memberRepository.save(member);
    }
    public Page<Member> getMemberList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));
        return memberRepository.findAllByKeyword(kw, pageable);
    }

    public List<Member> getMemberList() {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        return memberRepository.findAll();
    }

    public Member getMemberByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public void updateMemberStatusAndRole(long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        member.setIsActive(request.isActive());
        member.setRole(MemberRole.valueOf(request.getRole()));
        memberRepository.save(member);
    }
}

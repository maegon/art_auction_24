package com.example.ArtAuction_24.domain.member.entity;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.post.entity.Post;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.notification.entity.Notification;

import com.example.ArtAuction_24.recharge.entity.Recharge;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.management.relation.Role;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private String username; // 회원 아이디

    private String password; // 회원 비밀번호

    @Column(unique = true)
    private String email; // 이메일

    @Column(unique = true)
    private String nickname; // 닉네임

    private String phoneNumber; // 전화번호
    private String address;  // 주소
    private String providerTypeCode; //소셜 로그인인지 확인, 소셜로그인 할 경우 사용됨

    @Enumerated(EnumType.STRING)
    private MemberRole role; // 회원 권한

    @Column(unique = false)
    private String image; // 이미지 파일 이름을 저장

    private Boolean isActive = true; // Boolean으로 변경하고 기본값을 true로 설정, 사이트 활동 여부

    @Column(nullable = false)
    private Long balance = 0L; // 기본값을 0으로 설정, 회원이 가지고 있는 소지금

    @OneToMany(mappedBy = "member")
    private List<Recharge> rechargeList;


    @OneToMany(mappedBy = "member")
    private List<Bid> bidList;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
    private List<Notification> notifications; // 회원이 수신한 알림 목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Question> questionList; // 질문 목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Answer> answerList; // 답변목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<LikeProduct> likeProductList; // 답변목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> postList; // 포스트목록

    @OneToMany(mappedBy = "author")
    private List<Artist> artist;

    private transient String formattedbalance;

    private boolean agreedToTerms;
    private boolean proofSubmitted;
    private String proofFilePath;
    private boolean approvedArtist;
    private boolean agreePersonalInfo;
    private boolean agreeService;
    // Setter와 Getter
    // Setter for artistApplicationStatus
    @Setter
    @Getter
    private String artistApplicationStatus;


    @PostLoad
    private void postLoad() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        this.formattedbalance = formatter.format(this.balance);
    }

    public void deductBalance(BigDecimal amount) {
        if (this.balance >= amount.longValue()) {
            this.balance -= amount.longValue();
        } else {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }


    // 관리자가 회원의 isActive, Role 수정 시 필요함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SimpleGrantedAuthority는 권한을 나타내며, role 필드를 사용하여 권한을 부여합니다.
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;  // Spring Security가 인증 시 사용하는 비밀번호
    }

    @Override
    public String getUsername() {
        return this.username;  // Spring Security가 인증 시 사용하는 사용자 이름
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정이 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정이 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격 증명이 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;  // 계정 활성화 여부
    }

}
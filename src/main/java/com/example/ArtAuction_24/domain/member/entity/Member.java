package com.example.ArtAuction_24.domain.member.entity;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.notification.entity.Notification;
import com.example.ArtAuction_24.domain.review.entity.Review;
import com.example.ArtAuction_24.recharge.entity.Recharge;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.text.DecimalFormat;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String phoneNumber;
    private String address;
    private String providerTypeCode;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(unique = false)
    private String image; // 이미지 파일 이름을 저장

    @Column(nullable = false)
    private Boolean isActive = true; // Boolean으로 변경하고 기본값을 true로 설정

    @Column(nullable = false)
    private Long balance = 0L; // 기본값을 0으로 설정

    @OneToMany(mappedBy = "member")
    private List<Recharge> rechargeList;


    @OneToMany(mappedBy = "member")
    private List<Bid> bidList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Review> reviewList; // 리뷰 목록

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
    private List<Notification> notifications; // 회원이 수신한 알림 목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Question> questionList; // 질문 목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Answer> answerList; // 답변목록

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<LikeProduct> likeProductList; // 답변목록

    private transient String formattedbalance;


    @PostLoad
    private void postLoad() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        this.formattedbalance = formatter.format(this.balance);
    }
}
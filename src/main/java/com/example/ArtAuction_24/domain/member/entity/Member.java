package com.example.ArtAuction_24.domain.member.entity;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.notification.entity.Notification;
import com.example.ArtAuction_24.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String providerTypeCode;

    @Column(unique = false)
    private String image; // 이미지 파일 이름을 저장

    private String isActive;


    private Long balance; //충전 잔액

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
}
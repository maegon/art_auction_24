package com.example.ArtAuction_24.member.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import com.example.ArtAuction_24.bid.entity.Bid;
import com.example.ArtAuction_24.notification.entity.Notification;
import com.example.ArtAuction_24.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;

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
}
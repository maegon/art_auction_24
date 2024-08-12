package com.example.ArtAuction_24.recharge.entity;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Recharge extends BaseEntity { // 충전(사용 X)


    @Column(nullable = false)
    private Long amount; // 충전액

    @Column(nullable = false)
    private LocalDateTime rechargeDate = LocalDateTime.now(); // 기본값 설정

    @Column(nullable = false)
    private String orderId; // 추가된 필드

    @ManyToOne(optional = false)
    private Member member;

}

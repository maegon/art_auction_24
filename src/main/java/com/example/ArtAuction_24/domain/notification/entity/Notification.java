package com.example.ArtAuction_24.domain.notification.entity;

import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import com.example.ArtAuction_24.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    private String message; // 알림 내용
    private boolean isRead; // 읽음 여부

    @ManyToOne
    private Member recipient; // 수신자
}
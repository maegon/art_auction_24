package com.example.ArtAuction_24.domain.notification.entity;

import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import com.example.ArtAuction_24.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    private String message; // 알림 내용
    private String recipient; // 수신자 정보 (이메일, 전화번호 등)
    private boolean isSent; // 발송 여부
    private LocalDateTime sentAt; // 발송 시간

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 유형

    // 알림 유형 enum
    public enum NotificationType {
        EMAIL,
        SMS,
        PUSH
    }


}
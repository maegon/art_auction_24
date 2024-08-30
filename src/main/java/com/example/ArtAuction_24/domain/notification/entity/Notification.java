package com.example.ArtAuction_24.domain.notification.entity;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.*;
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

    @Lob
    private String message; // 알림 내용

    private String recipient; // 수신자 정보 (이메일, 전화번호 등)
    private String subject;
    private boolean isSent; // 발송 여부
    private LocalDateTime sentAt; // 발송 시간

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 유형

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; // 관련 상품

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // 관련 회원

    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist; // 관련 작가

    // 알림 유형 enum
    public enum NotificationType {
        EMAIL,
        SMS,
        PUSH
    }
}

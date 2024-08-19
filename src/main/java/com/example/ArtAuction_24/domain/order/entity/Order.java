package com.example.ArtAuction_24.domain.order.entity;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    // 상품의 가격 (현재 입찰가)
    @Column(nullable = false)
    private BigDecimal productPrice;

    // 그림 제목
    @Column(nullable = false)
    private String productTitle;

    // 송장 번호
    @Column(nullable = false, unique = true)
    private String trackingNumber;

    // 그림 사진 URL
    @Column(nullable = false)
    private String productThumbnail;

    // 낙찰자의 주소
    @Column(nullable = false)
    private String bidderAddress;

    // 낙찰자의 이름
    @Column(nullable = false)
    private String bidderName;

    // 낙찰자의 번호
    @Column(nullable = false)
    private String bidderPhone;



    // 관계 설정: 주문과 관련된 상품 엔티티
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 관계 설정: 주문과 관련된 사용자 엔티티 (낙찰자)
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member winningBidder;

    // 관계 설정: 주문과 관련된 작가 엔티티
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;  // Enum 타입으로 주문 상태 관리
}

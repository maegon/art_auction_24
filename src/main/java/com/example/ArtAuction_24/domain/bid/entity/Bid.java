package com.example.ArtAuction_24.domain.bid.entity;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Bid extends BaseEntity { // 특정 제품에 대한 개별 입찰.


    private BigDecimal amount;
    private LocalDateTime bidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 입찰한 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 입찰된 제품

}

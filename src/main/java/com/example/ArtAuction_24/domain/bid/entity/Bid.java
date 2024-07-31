package com.example.ArtAuction_24.domain.bid.entity;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.Entity;
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

    @ManyToOne
    private Member member;

    @ManyToOne
    private AuctionProduct product;

}

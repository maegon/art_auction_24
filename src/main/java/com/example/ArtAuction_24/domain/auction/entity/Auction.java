package com.example.ArtAuction_24.domain.auction.entity;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;

import jakarta.persistence.Entity;

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
public class Auction extends BaseEntity { // 여러 제품을 경매에 올려 판매하는 이벤트.


    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String status; // 경매 상태 (예: 활성화, 종료, 취소)

    @OneToMany(mappedBy = "auction")
    private List<AuctionProduct> products;


}

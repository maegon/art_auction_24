package com.example.ArtAuction_24.bid.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import com.example.ArtAuction_24.member.entity.Member;
import com.example.ArtAuction_24.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private Product product;

}
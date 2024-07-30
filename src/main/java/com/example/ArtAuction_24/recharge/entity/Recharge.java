package com.example.ArtAuction_24.recharge.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import com.example.ArtAuction_24.member.entity.Member;
import com.example.ArtAuction_24.product.entity.AuctionProduct;
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
public class Recharge extends BaseEntity { // 충전


    private Long amount;
    private LocalDateTime rechargeDate;

    @ManyToOne
    private Member member;


}

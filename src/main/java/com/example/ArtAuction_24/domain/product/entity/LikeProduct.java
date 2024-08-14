package com.example.ArtAuction_24.domain.product.entity;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LikeProduct extends BaseEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    // equals()와 hashCode()를 오버라이드하여 동일한 찜을 중복해서 저장하지 않도록 함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeProduct that = (LikeProduct) o;
        return Objects.equals(member, that.member) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, product);
    }

}

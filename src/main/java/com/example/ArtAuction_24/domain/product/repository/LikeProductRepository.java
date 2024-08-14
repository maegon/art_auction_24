package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.ExhibitionProduct;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeProductRepository extends JpaRepository<LikeProduct, Long> {
    Optional<LikeProduct> findByProductIdAndMemberId(Long productId, Long memberId);
    List<LikeProduct> findByMember(Member member);
    List<LikeProduct> findByMemberId(Long memberId);
}

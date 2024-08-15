package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {
    List<AuctionProduct> findByAuctionId(Long auctionId);

    Optional<AuctionProduct> findByProductId(Long productId);

    // 특정 제품에 대해 가장 최근 경매를 반환하는 쿼리 메서드
    Optional<AuctionProduct> findTopByProductIdOrderByAuctionCreateDateDesc(Long productId);

}

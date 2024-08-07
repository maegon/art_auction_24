package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {
    List<AuctionProduct> findByAuctionId(Long auctionId);

    Optional<AuctionProduct> findByProductId(Long productId);
}

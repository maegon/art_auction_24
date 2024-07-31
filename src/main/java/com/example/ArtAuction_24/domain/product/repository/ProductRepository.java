package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<AuctionProduct, Long> {

}

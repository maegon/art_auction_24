package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {

    Page<AuctionProduct> findAll(Pageable pageable);

    @Query("""
            select distinct p
            from AuctionProduct p
            where p.title like %:kw%
            or p.description like %:kw%
            """)
    Page<AuctionProduct> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}

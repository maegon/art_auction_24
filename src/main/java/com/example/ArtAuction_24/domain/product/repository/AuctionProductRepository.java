package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {

    @Query("SELECT a FROM AuctionProduct a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AuctionProduct> findByKeyword(@Param("keyword") String keyword);

    Page<AuctionProduct> findAll(Pageable pageable);

    @Query("""
            SELECT p
            FROM AuctionProduct p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%'))
            """)
    Page<AuctionProduct> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    List<AuctionProduct> findAllByOrderByCreateDateDesc();


}

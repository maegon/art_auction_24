package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT a FROM Product a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> findByKeyword(@Param("keyword") String keyword);

    Page<Product> findAll(Pageable pageable);

    @Query("""
            SELECT p
            FROM Product p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%'))
            """)
    Page<Product> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    List<Product> findAllByOrderByCreateDateDesc();


    Product findTopByOrderByViewDesc();

    Page<Product> findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(String title, String korName, String engName, Pageable pageable);

}

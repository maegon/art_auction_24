package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
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
            WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')))
            AND (p IN (SELECT ap.product FROM AuctionProduct ap JOIN ap.auction a WHERE a.status = 'ACTIVE'))
            """)
    Page<Product> findByKeywordAndAuctionActive(@Param("kw") String kw, Pageable pageable);

    @Query("""
            SELECT p
            FROM Product p
            WHERE p IN (SELECT ap.product FROM AuctionProduct ap JOIN ap.auction a WHERE a.status = 'ACTIVE')
            """)
    Page<Product> findByAuctionActive(Pageable pageable);

    List<Product> findAllByOrderByCreateDateDesc();

    Product findTopByOrderByViewDesc();

    Page<Product> findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(String title, String korName, String engName, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.auctions")
    List<Product> findAllWithAuction();

    @Query("""
            SELECT DISTINCT p
            FROM Product p
            JOIN p.auctions a
            WHERE a.status = 'ACTIVE'
            """)
    List<Product> findProductsByActiveAuctions();

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN p.auctions a
    WHERE a.status = 'ACTIVE' AND
          (:keyword IS NULL OR p.title LIKE %:keyword%) AND
          (:auctionName IS NULL OR :auctionName = '' OR a.name = :auctionName)
""")
    Page<Product> findByActiveAuctionsAndFilter(@Param("keyword") String keyword, @Param("auctionName") String auctionName, Pageable pageable);

    // ACTIVE 상태의 경매에 포함된 제품을 찾기 위한 메소드
    @Query("SELECT p FROM Product p JOIN p.auctions a WHERE a.status = :status")
    List<Product> findProductsByAuctionStatus(@Param("status") AuctionStatus status);


    List<Product> findAllByAuctions(Auction auction);


    List<Product> findByArtistId(Long artistId);


    List<Product> findByWinningBidderIsNull();

    @Query("""
    SELECT p FROM Product p
    LEFT JOIN p.auctions a
    WHERE p.winningBidder IS NULL AND
          (a IS NULL OR a.status NOT IN ('ACTIVE', 'SCHEDULED'))
    """)
    List<Product> findAvailableProducts();

    List<Product> findByArtist(Artist artist);

    List<Product> findByAuctionedTrueAndApprovedFalse(); // 승인 대기 중인 제품
    List<Product> findByAuctionedTrueAndApprovedTrue();  // 승인된 제품
    // 관리자가 경매 제품을 선택할때 낙찰자가 존재하는 제품과 이미 경매에 올라가 있는 제품 제외

    boolean existsByTitleAndArtist(String title, Artist artist);
}

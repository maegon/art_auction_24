package com.example.ArtAuction_24.domain.auction.repository;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // 특정 상태의 경매 중에서 종료 시간이 현재 시간보다 이전인 경매를 조회
    List<Auction> findByEndDateBeforeAndStatus(LocalDateTime endDate, AuctionStatus status);

    // 특정 상태의 경매를 모두 조회
    List<Auction> findByStatus(AuctionStatus status);

    // CLOSED 상태가 아닌 경매의 이름을 중복 없이 조회
    @Query("SELECT DISTINCT a.name FROM Auction a WHERE a.status NOT IN ('CLOSED', 'SCHEDULED')")
    List<String> findDistinctAuctionNames();


    // 특정 상태의 경매 중에서 시작 시간이 현재 시간보다 이전인 경매를 조회
    List<Auction> findByStartDateBeforeAndStatus(LocalDateTime now, AuctionStatus status);

    // 특정 상품 ID에 대해 종료일이 가장 최신인 경매를 찾는 메서드

}

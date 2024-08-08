package com.example.ArtAuction_24.domain.auction.repository;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByEndDateBeforeAndStatus(LocalDateTime endDate, AuctionStatus status);

    // 특정 상태의 경매를 조회하는 메서드
    List<Auction> findByStatus(AuctionStatus status);


}

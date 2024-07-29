package com.example.ArtAuction_24.auction.repository;

import com.example.ArtAuction_24.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Bid, Long> {
}

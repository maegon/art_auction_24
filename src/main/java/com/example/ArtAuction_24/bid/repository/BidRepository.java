package com.example.ArtAuction_24.bid.repository;

import com.example.ArtAuction_24.answer.entity.Answer;
import com.example.ArtAuction_24.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
}

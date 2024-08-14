package com.example.ArtAuction_24.domain.bid.repository;

import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    Bid findTopByProductOrderByAmountDesc(Product product);


    List<Bid> findAllByProduct(Product product);

    List<Bid> findAllByProductOrderByAmountDesc(Product product);

    Optional<Bid> findFirstByProductIdAndMemberIdOrderByBidTimeDesc(Long productId, Long memberId);

    List<Bid> findAllByProductOrderByBidTimeDesc(Product product);
}

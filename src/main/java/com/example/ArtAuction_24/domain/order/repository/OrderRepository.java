package com.example.ArtAuction_24.domain.order.repository;



import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByWinningBidderId(Long winningBidderId); // 낙찰자가 볼 수 있는 주문 목록
    List<Order> findByArtistId(Long artistId); // 작가가 볼 수 있는 주문 목록

    // 중복 확인을 위한 메서드
    boolean existsByProductIdAndWinningBidderId(Long productId, Long memberId);
}

package com.example.ArtAuction_24.domain.order.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.notification.service.NotificationService;
import com.example.ArtAuction_24.domain.order.entity.Order;
import com.example.ArtAuction_24.domain.order.entity.OrderStatus;
import com.example.ArtAuction_24.domain.order.repository.OrderRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Transactional
    public void createOrderForAuction(Auction auction) {

        for (Product product : auction.getProducts()) {
            if (product.getWinningBidder() != null) {
                Member winningBidder = product.getWinningBidder();
                Artist artist = product.getArtist(); // 작가의 Member를 가져옵니다.

                // 기존 주문이 있는지 확인
                if (orderExists(product.getId(), winningBidder.getId())) {
                    logger.info("Order already exists for product ID {} and bidder ID {}", product.getId(), winningBidder.getId());
                    continue; // 중복 주문 방지
                }


                // 새로운 주문을 생성
                Order order = Order.builder()
                        .createDate(LocalDateTime.now())
                        .productPrice(product.getCurrentPrice())
                        .productTitle(product.getTitle())
                        .trackingNumber(generateTrackingNumber())
                        .productThumbnail(product.getThumbnailImg())
                        .bidderAddress(winningBidder.getAddress())
                        .bidderName(winningBidder.getNickname())
                        .bidderPhone(winningBidder.getPhoneNumber())
                        .product(product)
                        .winningBidder(winningBidder)
                        .artist(artist)
                        .status(OrderStatus.PENDING)  // 초기 상태 설정
                        .build();

                try {
                    orderRepository.save(order);
                    logger.info("Order created successfully for product ID {}", product.getId());
                } catch (Exception e) {
                    logger.error("Failed to save order for product ID {}: ", product.getId(), e);
                }
            }
        }
    }

    // 송장 번호 생성 로직 (UUID 기반)
    private String generateTrackingNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    // 중복 주문 확인
    private boolean orderExists(Long productId, Long memberId) {
        return orderRepository.existsByProductIdAndWinningBidderId(productId, memberId);
    }

    // 주문 목록을 현재 사용자 ID로 가져오는 메서드
    public List<Order> getOrdersByMemberId(Long memberId) {
        return orderRepository.findByWinningBidderId(memberId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}

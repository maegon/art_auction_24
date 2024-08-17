package com.example.ArtAuction_24.domain.bid.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.notification.service.NotificationService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.bid.repository.BidRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final AuctionRepository auctionRepository;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);


    @Transactional
    public void placeBid(Long productId, Long memberId, BigDecimal bidAmount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 입찰 금액이 현재 입찰가보다 높은지 확인
        if (bidAmount.compareTo(product.getCurrentBid()) <= 0) {
            throw new IllegalArgumentException("입찰 금액이 현재 입찰가보다 높아야 합니다.");
        }

        // 회원 잔액이 입찰 금액보다 많은지 확인
        if (member.getBalance() < bidAmount.longValue()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        product.setPreviousBid(product.getCurrentBid());


        // 상품의 현재 입찰가 업데이트
        product.setCurrentBid(bidAmount);
        product.setWinningBidder(null);
        productRepository.save(product);

        // 입찰 정보 저장
        Bid bid = Bid.builder()
                .amount(bidAmount)
                .bidTime(LocalDateTime.now())
                .member(member)
                .product(product)
                .build();

        bidRepository.save(bid);

        // 현재 입찰자의 잔액 차감 (경매 종료 시점에서 처리됨)
    }

    @Transactional
    public void finalizeAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("경매를 찾을 수 없습니다."));

        // 경매에 포함된 모든 제품에 대해 최종 입찰자 처리
        for (Product product : auction.getProducts()) {
            List<Bid> bids = bidRepository.findAllByProductOrderByAmountDesc(product);
            if (bids.isEmpty()) {
                logger.warn("경매에 입찰이 없습니다: 제품 ID {}", product.getId());
                continue;
            }
            System.out.println("====================== test2 ======================");
            boolean winningBidProcessed = false;

            for (Bid bid : bids) {
                Member winningBidder = bid.getMember();

                // 최종 입찰자의 잔액이 충분한지 확인
                if (winningBidder.getBalance() >= bid.getAmount().longValue()) {

                    // 잔액 차감
                    winningBidder.setBalance(winningBidder.getBalance() - bid.getAmount().longValue());
                    memberRepository.save(winningBidder);

                    // 제품의 낙찰자 설정
                    product.setWinningBidder(winningBidder);
                    productRepository.save(product);

                    // 경매 종료 결과를 알림
                    notificationService.notifyAuctionResults(auction);

                    winningBidProcessed = true;
                    break;  // 유효한 입찰자가 있으면 종료
                } else {
                    logger.warn("회원의 잔액이 부족합니다. ID: {}, 입찰 금액: {}", winningBidder.getId(), bid.getAmount());
                }
            }

            if (!winningBidProcessed) {
                logger.error("모든 입찰자가 잔액이 부족합니다. 제품 ID: {}", product.getId());
                auction.setStatus(AuctionStatus.CANCELLED);
                auctionRepository.save(auction);

                // 제품의 입찰가를 이전 상태로 복원
                product.setCurrentBid(product.getStartingPrice());
                product.setWinningBidder(null);
                productRepository.save(product);
            }
        }
    }

    public List<Bid> findBidsByProduct(Product product) {
        return bidRepository.findAllByProduct(product);
    }

    @Transactional
    public void cancelBid(Long bidId, Long memberId) {
        // 1. 입찰 정보 로드
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new IllegalArgumentException("입찰을 찾을 수 없습니다."));

        Product product = bid.getProduct();
        Member member = bid.getMember();

        // 2. 가장 최근의 입찰을 조회
        List<Bid> recentBids = bidRepository.findAllByProductOrderByBidTimeDesc(product);

        // 3. 취소하려는 입찰이 가장 최근의 입찰인지 확인
        if (recentBids.size() > 0 && Objects.equals(recentBids.get(0).getId(), bid.getId())) {
            // 가장 최근의 입찰이 맞다면, 입찰가 롤백 처리
            if (recentBids.size() > 1) {
                product.setCurrentBid(recentBids.get(1).getAmount());
            } else {
                // 이전 입찰가가 없는 경우 시작 가격으로 롤백
                product.setCurrentBid(product.getStartingPrice());
            }
            productRepository.save(product);
        }

        // 4. 입찰 취소 처리 - 특정 회원의 입찰 기록만 삭제
        if (Objects.equals(member.getId(), memberId)) {
            bidRepository.delete(bid);
        }

        // 5. (잔액 회복 처리 제거)
    }






    public Optional<Bid> findCurrentBidByProductAndMember(Long productId, Long memberId) {
        return bidRepository.findFirstByProductIdAndMemberIdOrderByBidTimeDesc(productId, memberId);
    }


    public Optional<Bid> findById(Long bidId) {
        return bidRepository.findById(bidId);
    }
}

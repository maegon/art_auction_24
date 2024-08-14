package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.bid.service.BidService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import io.micrometer.common.KeyValues;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final BidService bidService;
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

    public Auction create(String name, LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        Set<Product> productSet = new HashSet<>(products);

        AuctionStatus auctionStatus = startDate.isAfter(LocalDateTime.now()) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;

        Auction auction = Auction.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .status(auctionStatus)
                .products(productSet)
                .build();

        return auctionRepository.save(auction);
    }

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void updateAuctionStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // 만료된 경매를 종료합니다.
        List<Auction> expiredAuctions = auctionRepository.findByEndDateBeforeAndStatus(now, AuctionStatus.ACTIVE);
        for (Auction auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been closed.", auction.getId());

            // 경매 종료 후 최종 입찰자의 잔액 차감
            bidService.finalizeAuction(auction.getId());  // 경매 ID로 호출
        }

        // 예약된 경매를 활성화합니다.
        List<Auction> scheduledAuctions = auctionRepository.findByStartDateBeforeAndStatus(now, AuctionStatus.SCHEDULED);
        for (Auction auction : scheduledAuctions) {
            auction.setStatus(AuctionStatus.ACTIVE);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been activated.", auction.getId());
        }
    }

    private void closeAuction(Auction auction) {
        // 경매에 참여한 제품 목록 가져오기
        List<Product> products = productService.findAllByAuction(auction);

        for (Product product : products) {
            // KeyValues 대신 List<Bid>를 직접 반환받습니다.
            List<Bid> bids = bidService.findBidsByProduct(product); // 수정된 부분

            // 가장 높은 입찰가를 찾습니다.
            Bid winningBid = bids.stream()
                    .max((b1, b2) -> b1.getAmount().compareTo(b2.getAmount()))
                    .orElse(null);

            if (winningBid != null) {
                Member winningMember = winningBid.getMember();
                BigDecimal winningAmount = winningBid.getAmount();

                // 승자의 잔액 차감
                if (winningMember.getBalance() >= winningAmount.longValue()) {
                    winningMember.setBalance(winningMember.getBalance() - winningAmount.longValue());
                    memberService.save(winningMember);

                    // 상품의 상태 업데이트
                    product.setWinningBidder(winningMember);
                    productService.save(product);
                } else {
                    logger.warn("Insufficient balance for member with ID {}.", winningMember.getId());
                }
            }
        }

        // 경매 상태를 닫힘으로 설정
        auction.setStatus(AuctionStatus.CLOSED);
        auctionRepository.save(auction);
    }




    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }

    public List<Auction> getAuctionsByStatus(AuctionStatus status) {
        return auctionRepository.findByStatus(status);
    }

    public Page<Product> getProductsWithFilteringAndSorting(String keyword, String auctionName, Pageable pageable, String sort) {
        Sort sortOrder = switch (sort) {
            case "price-asc" -> Sort.by("startingPrice").ascending();
            case "price-desc" -> Sort.by("startingPrice").descending();
            case "latest" -> Sort.by("auctionStartDate").descending();
            default -> Sort.by("auctionStartDate").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);

        Page<Product> products = productRepository.findByActiveAuctionsAndFilter(keyword, auctionName, sortedPageable);

        return products;
    }

    public List<String> getDistinctAuctionNames() {
        return auctionRepository.findDistinctAuctionNames();
    }

    // 진행중인 경매(ACTIVE) 목록을 반환하는 메소드
    public List<Auction> findActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    // 예정된 경매(SCHEDULED) 목록을 반환하는 메소드
    public List<Auction> getScheduledAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.SCHEDULED);
    }



}


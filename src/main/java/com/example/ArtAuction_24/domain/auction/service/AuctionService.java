package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
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
    private final AuctionProductRepository auctionProductRepository;
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
        }

        // 예약된 경매를 활성화합니다.
        List<Auction> scheduledAuctions = auctionRepository.findByStartDateBeforeAndStatus(now, AuctionStatus.SCHEDULED);
        for (Auction auction : scheduledAuctions) {
            auction.setStatus(AuctionStatus.ACTIVE);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been activated.", auction.getId());
        }
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


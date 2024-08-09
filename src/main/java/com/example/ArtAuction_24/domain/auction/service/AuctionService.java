package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
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
    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);
    public Auction create(String name, LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        Set<Product> productSet = new HashSet<>(products);

        Auction auction = Auction.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .status(AuctionStatus.ACTIVE)
                .products(productSet)
                .build();
        return auctionRepository.save(auction);
    }

    @Scheduled(fixedDelay = 10000) // 1분마다 실행
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> expiredAuctions = auctionRepository.findByEndDateBeforeAndStatus(now, AuctionStatus.ACTIVE);
        for (Auction auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been closed.", auction.getId());
        }
    }


    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }

    public List<Auction> getAuctionsByStatus(AuctionStatus status) {
        return auctionRepository.findByStatus(status);
    }

    // AuctionService.java
    public Page<Product> getProductsWithFilteringAndSorting(String keyword, String auctionName, Pageable pageable, String sort) {
        Sort sortOrder = switch (sort) {
            case "price-asc" -> Sort.by("startingPrice").ascending();
            case "price-desc" -> Sort.by("startingPrice").descending();
            case "latest" -> Sort.by("auctionStartDate").descending();
            default -> Sort.by("auctionStartDate").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);

        // status가 ACTIVE인 경매에 포함된 제품만 필터링 및 auctionName 필터링 추가
        Page<Product> products = productRepository.findByActiveAuctionsAndFilter(keyword, auctionName, sortedPageable);

        return products;
    }


    // 중복 제거된 카테고리 이름 목록을 가져오는 메서드
    public List<String> getDistinctAuctionNames() {
        return auctionRepository.findDistinctAuctionNames();
    }

}

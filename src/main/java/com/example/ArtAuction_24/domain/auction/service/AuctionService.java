package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> expiredAuctions = auctionRepository.findByEndDateBeforeAndStatus(now, AuctionStatus.ACTIVE);
        for (Auction auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been closed.", auction.getId());
        }
    }
}

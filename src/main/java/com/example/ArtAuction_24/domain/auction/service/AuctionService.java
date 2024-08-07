package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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

    public Auction create(String name, LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        Set<Product> productSet = new HashSet<>(products);

        Auction auction = Auction.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .products(productSet)
                .build();
        return auctionRepository.save(auction);
    }
}

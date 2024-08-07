package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionProductService {

    private final AuctionProductRepository auctionProductRepository;

    public AuctionProduct createAuctionProduct(AuctionProduct auctionProduct) {
        return auctionProductRepository.save(auctionProduct);
    }

    public Optional<AuctionProduct> findById(Long id) {
        return auctionProductRepository.findById(id);
    }

    public List<AuctionProduct> findByAuctionId(Long auctionId) {
        return auctionProductRepository.findByAuctionId(auctionId);
    }

    @Transactional
    public void deleteById(Long id) {
        auctionProductRepository.deleteById(id);
    }

    public Optional<AuctionProduct> getAuctionProductByProductId(Long productId) {
        return auctionProductRepository.findByProductId(productId);
    }

}

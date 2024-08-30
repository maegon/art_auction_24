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

    // 해당 product_id가 auction_product에 존재하는지 확인
    public boolean existsByProductId(long productId) {
        return auctionProductRepository.existsByProductId(productId);
    }

    // 해당 product_id와 관련된 auction_product 레코드 삭제
    @Transactional
    public void deleteByProductId(long productId) {
        auctionProductRepository.deleteByProductId(productId);
    }
}

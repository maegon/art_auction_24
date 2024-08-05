package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionProductService {
    private final AuctionProductRepository auctionProductRepository;

    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    public Page<AuctionProduct> getProductsWithSorting(String keyword, Pageable pageable, String sortOption) {
        Sort sort = switch (sortOption) {
            case "price-asc" -> Sort.by("startingPrice").ascending();
            case "price-desc" -> Sort.by("startingPrice").descending();
            case "latest" -> Sort.by("createDate").descending();
            default -> Sort.by("createDate").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (keyword != null && !keyword.isEmpty()) {
            return auctionProductRepository.findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(keyword, keyword, keyword, sortedPageable);
        } else {
            return auctionProductRepository.findAll(sortedPageable);
        }
    }

    public void create(String title, String description, String medium, String dimensions,
                       BigDecimal startingPrice, BigDecimal currentBid,
                       LocalDateTime auctionStartDate, String thumbnailImg, String category, Artist artist) {
        AuctionProduct p = AuctionProduct.builder()
                .title(title)
                .description(description)
                .medium(medium)
                .dimensions(dimensions)
                .startingPrice(startingPrice)
                .currentBid(currentBid)  // startingPrice와 동일하게 설정
                .auctionStartDate(auctionStartDate)
                .thumbnailImg(thumbnailImg)
                .category(category)
                .artist(artist)
                .build();
        auctionProductRepository.save(p);
    }

    public AuctionProduct getProduct(Long id) {
        return auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<AuctionProduct> getList() {
        return auctionProductRepository.findAll();
    }

    public List<AuctionProduct> findByKeyword(String keyword) {
        return auctionProductRepository.findByKeyword(keyword);
    }

    public List<AuctionProduct> findAllAuctionProductOrderByCreateDateDesc() {
        return auctionProductRepository.findAllByOrderByCreateDateDesc();
    }

    public AuctionProduct getTopAuctionProductByView() {
        return auctionProductRepository.findTopByOrderByViewDesc();
    }

    @Transactional
    public void incrementViews(Long id) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuctionProduct not found"));
        auctionProduct.setView(auctionProduct.getView() + 1);
        auctionProductRepository.save(auctionProduct);
    }

    public Page<AuctionProduct> searchProducts(String keyword, Pageable pageable) {
        return auctionProductRepository.findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(keyword, keyword, keyword, pageable);
    }

    public Page<AuctionProduct> getAuctionProducts(Pageable pageable) {
        return auctionProductRepository.findAll(pageable);
    }
}
package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<AuctionProduct> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return auctionProductRepository.findAllByKeyword(kw, pageable);
    }

    public void create(String title, String description, String medium, String dimensions,
                       int startingPrice, int currentBid,
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
        Optional<AuctionProduct> product = auctionProductRepository.findById(id);

        if (product.isPresent()) {
            return product.get();
        } else {
            throw new RuntimeException("product not found");
        }
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


}

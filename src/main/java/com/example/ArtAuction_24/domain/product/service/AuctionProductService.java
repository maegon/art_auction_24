package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
    private final AuctionProductRepository productRepository;

    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    public Page<AuctionProduct> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return productRepository.findAllByKeyword(kw, pageable);
    }

    public void create(String title, String description, String medium, String dimensions,
                       BigDecimal startingPrice, BigDecimal currentBid,
                       LocalDateTime auctionStartDate, MultipartFile thumbnail, String category) {
        // 유효성 검사
        if (thumbnail.isEmpty() || startingPrice.compareTo(BigDecimal.ZERO) < 0 || currentBid.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid input data");
        }

        // 경매 시작 시 currentBid는 startingPrice와 동일
        if (!currentBid.equals(startingPrice)) {
            throw new IllegalArgumentException("Current bid must be equal to starting price at auction creation");
        }

        String thumbnailRelPath = "image/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailDir = new File(genFileDirPath + File.separator + "image");

        // 디렉토리 생성
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs();
        }

        File thumbnailFile = new File(thumbnailDir, thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save thumbnail", e);
        }

        AuctionProduct p = AuctionProduct.builder()
                .title(title)
                .description(description)
                .medium(medium)
                .dimensions(dimensions)
                .startingPrice(startingPrice)
                .currentBid(currentBid)  // startingPrice와 동일하게 설정
                .auctionStartDate(auctionStartDate)
                .thumbnailImg(thumbnailRelPath)
                .category(category)
                .build();
        productRepository.save(p);
    }

    public AuctionProduct getProduct(Long id) {
        Optional<AuctionProduct> product = productRepository.findById(id);

        if (product.isPresent()) {
            return product.get();
        } else {
            throw new RuntimeException("product not found");
        }
    }

    public List<AuctionProduct> getList() {
        return productRepository.findAll();
    }
}

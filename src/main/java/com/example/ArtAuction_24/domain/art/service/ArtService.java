package com.example.ArtAuction_24.domain.art.service;

import com.example.ArtAuction_24.domain.art.entity.Art;
import com.example.ArtAuction_24.domain.art.repository.ArtRepository;
import com.example.ArtAuction_24.domain.member.entity.Member;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArtService {
    private final ArtRepository artRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public Page<Art> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (kw == null || kw.trim().length() == 0) {
            return artRepository.findAll(pageable);
        }
        Specification<Art> spec = search(kw);
        return artRepository.findAll(spec, pageable);
    }

    private Specification<Art> search(String kw) {
        return new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Art> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Art, Member> u1 = r.join("author", JoinType.LEFT);
                return cb.or(cb.like(r.get("title"), "%" + kw + "%"), // 제목
                        cb.like(r.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"));    // 질문 작성자
            }
        };
    }

    public Art getArt(Integer id) {
        Optional<Art> of = artRepository.findById(id);
        if (of.isEmpty()) throw new DataNotFoundException("friend not found");
        return of.get();
    }

    // 생성자 주입
    public ArtService(ArtRepository artRepository) {
        this.artRepository = artRepository;
    }

    public Art create(MultipartFile thumbnail, String korTitle, String engTitle, String artist, String width, String height, String unit, String technique, String price, String place, String artIntroduction, Member member) {
        String thumbnailRelPath = "image/art/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Art art = Art.builder()
                .thumbnailImg(thumbnailRelPath)
                .korTitle(korTitle)
                .engTitle(engTitle)
                .artist(artist)
                .width(width)
                .height(height)
                .unit(unit)
                .technique(technique)
                .price(price)
                .place(place)
                .artIntroduction(artIntroduction)
                .author(member)
                .build();

        artRepository.save(art);  // 주입된 인스턴스를 사용하여 save 호출

        return art;
    }
}

package com.example.ArtAuction_24.domain.art.service;

import com.example.ArtAuction_24.domain.art.entity.Art;
import com.example.ArtAuction_24.domain.art.repository.ArtRepository;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ArtService {

    private final ArtRepository artRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public List<Art> getList() {
        return artRepository.findAll();
    }

    public Art create(MultipartFile thumbnail, String korTitle, String engTitle, String width, String height, String unit, String technique, String price, String place, String artIntroduction, Member member, Artist artist) {
        // 파일 저장 경로 설정
        String thumbnailRelPath = "image/art/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        // 디렉토리 생성 및 파일 업로드 처리
        File dir = new File(fileDirPath + "/image/art");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("디렉토리 생성 실패: " + dir.getAbsolutePath());
            }
        }

        try {
            thumbnail.transferTo(thumbnailFile);
            if (!thumbnailFile.exists()) {
                throw new RuntimeException("파일 저장 실패: " + thumbnailFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
        }

        // Art 객체 생성
        Art art = Art.builder()
                .thumbnailImg(thumbnailRelPath)
                .korTitle(korTitle)
                .engTitle(engTitle)
                .width(width)
                .height(height)
                .unit(unit)
                .technique(technique)
                .price(price)
                .place(place)
                .artIntroduction(artIntroduction)
                .author(member)
                .artist(String.valueOf(artist))
                .build();

        // 데이터베이스에 Art 객체 저장
        artRepository.save(art);
        return art;
    }


    public Art getArt(Integer id) {
        Optional<Art> of = artRepository.findById(id);
        if (of.isEmpty()) throw new DataNotFoundException("friend not found");
        return of.get();
    }

    public Optional<Art> getArtsByMember(Member currentMember) {
        return artRepository.findByAuthor(currentMember);
    }

    public void deleteAllByArtist(Artist artist) {
    }

    public void modify(Art art, MultipartFile thumbnail, String korTitle, String engTitle, String width, String height, String unit, String technique, String price, String place, String artIntroduction) {

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailRelPath = "image/art/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

            try {
                thumbnail.transferTo(thumbnailFile);
                art.setThumbnail(thumbnailRelPath);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
            }
        }

        art.setKorTitle(korTitle);
        art.setEngTitle(engTitle);
        art.setWidth(width);
        art.setHeight(height);
        art.setUnit(unit);
        art.setTechnique(technique);
        art.setPrice(price);
        art.setPlace(place);
        art.setArtIntroduction(artIntroduction);
        artRepository.save(art);
    }

    public void delete(Art art) {
        artRepository.delete(art);
    }
}
package com.example.ArtAuction_24.domain.artist.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.repository.ArtistRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import com.example.ArtAuction_24.domain.member.entity.Member;
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
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public Artist create(MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType, Member member) {
        String thumbnailRelPath = "image/artist/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Artist artist = Artist.builder()
                .thumbnailImg(thumbnailRelPath)
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .tel(tel)
                .mail(mail)
                .mailType(mailType)
                .author(member)
                .build();
        artistRepository.save(artist);

        return artist;
    }

    public Artist create(String korName, String engName, String birthDate, String tel, String mail, String mailType, String introduce) {

        Artist artist = Artist.builder()
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .tel(tel)
                .mail(mail)
                .mailType(mailType)
                .introduce(introduce)
                .build();
        artistRepository.save(artist);

        return artist;
    }

    public Artist getArtist(Long id) {
        Optional<Artist> of = artistRepository.findById(id);
        if (of.isEmpty()) throw new DataNotFoundException("artist not found");
        return of.get();
    }

    public void modify(Artist artist, MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType, String introduce, String majorWork, String title, String content) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailRelPath = "image/artist/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

            try {
                thumbnail.transferTo(thumbnailFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            artist.setThumbnail(thumbnailRelPath);
        }
        artist.setKorName(korName);
        artist.setEngName(engName);
        artist.setBirthDate(birthDate);
        artist.setTel(tel);
        artist.setMail(mail);
        artist.setMailType(mailType);
        artist.setIntroduce(introduce);
        artist.setMajorWork(majorWork);
        artist.setTitle(title);
        artist.setContent(content);

        artistRepository.save(artist);
    }

    public void delete(Artist artist) {
        artistRepository.delete(artist);
    }

    public List<Artist> findByKeyword(String keyword) {
        return artistRepository.findByKeyword(keyword);
    }

    public Artist getArtistByKorName(String korName) {
        Optional<Artist> artistOptional = artistRepository.findByKorName(korName);
        return artistOptional.orElseThrow(() -> new DataNotFoundException("Artist not found with korName: " + korName));
    }
}

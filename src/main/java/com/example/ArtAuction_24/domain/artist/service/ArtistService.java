package com.example.ArtAuction_24.domain.artist.service;

import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.repository.*;
import com.example.ArtAuction_24.global.DataNotFoundException;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistAddRepository artistAddRepository;
    private final TitleAddRepository titleAddRepository;
    private final ContentAddRepository contentAddRepository;
    private final TitleContentAddRepository titleContentAddRepository;
    private final YearContentAddRepository yearContentAddRepository;
    private final WidthContentAddRepository widthContentAddRepository;
    private final HeightContentAddRepository heightContentAddRepository;
    private final UnitContentAddRepository unitContentAddRepository;
    private final TechniqueContentAddRepository techniqueContentAddRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public Artist create(MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType, Member member) {
        String thumbnailRelPath = "image/artist/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        File dir = new File(fileDirPath + "/image/artist");
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

    public Artist create(String korName, String engName, String birthDate, String tel, String mail, String mailType, String introduce, String majorWork) {
        Artist artist = Artist.builder()
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .tel(tel)
                .mail(mail)
                .mailType(mailType)
                .introduce(introduce)
                .majorWork(majorWork)
                .build();
        artistRepository.save(artist);

        return artist;
    }

    public Artist getArtist(Integer id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("artist not found"));
    }

    public void modify(Artist artist, MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType, String introduce,
                       List<String> artistAdds, List<String> titleAdds, List<String> contentAdds,
                       List<String> titleContentAdds, List<String> yearContentAdds, List<String> widthContentAdds, List<String> heightContentAdds, List<String> unitContentAdds, List<String> techniqueContentAdds) {

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailRelPath = "image/artist/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

            try {
                thumbnail.transferTo(thumbnailFile);
                artist.setThumbnail(thumbnailRelPath);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
            }
        }

        artist.setKorName(korName);
        artist.setEngName(engName);
        artist.setBirthDate(birthDate);
        artist.setTel(tel);
        artist.setMail(mail);
        artist.setMailType(mailType);
        artist.setIntroduce(introduce);

        updateArtistAdds(artist, artistAdds);
        updateTitleAdds(artist, titleAdds);
        updateContentAdds(artist, contentAdds);
        updateTitleContentAdds(artist, titleContentAdds);
        updateYearContentAdds(artist, yearContentAdds);
        updateWidthContentAdds(artist, widthContentAdds);
        updateHeightContentAdds(artist, heightContentAdds);
        updateUnitContentAdds(artist, unitContentAdds);
        updateTechniqueContentAdds(artist, techniqueContentAdds);

        artistRepository.save(artist);
    }

    private void updateArtistAdds(Artist artist, List<String> artistAdds) {
        if (artistAdds != null) {
            List<ArtistAdd> artistAddList = new ArrayList<>();
            for (String content : artistAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    ArtistAdd artistAdd = new ArtistAdd();
                    artistAdd.setContent(content);
                    artistAdd.setArtist(artist);
                    artistAddList.add(artistAdd);
                }
            }
            artistAddRepository.saveAll(artistAddList);
        }
    }

    private void updateTitleAdds(Artist artist, List<String> titleAdds) {
        if (titleAdds != null) {
            List<TitleAdd> titleAddList = new ArrayList<>();
            for (String content : titleAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    TitleAdd titleAdd = new TitleAdd();
                    titleAdd.setContent(content);
                    titleAdd.setArtist(artist);
                    titleAddList.add(titleAdd);
                }
            }
            titleAddRepository.saveAll(titleAddList);
        }
    }

    private void updateContentAdds(Artist artist, List<String> contentAdds) {
        if (contentAdds != null) {
            List<ContentAdd> contentAddList = new ArrayList<>();
            for (String content : contentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    ContentAdd contentAdd = new ContentAdd();
                    contentAdd.setContent(content);
                    contentAdd.setArtist(artist);
                    contentAddList.add(contentAdd);
                }
            }
            contentAddRepository.saveAll(contentAddList);
        }
    }

    private void updateTitleContentAdds(Artist artist, List<String> titleContentAdds) {
        if (titleContentAdds != null) {
            List<TitleContentAdd> titleContentAddList = new ArrayList<>();
            for (String content : titleContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    TitleContentAdd titleContentAdd = new TitleContentAdd();
                    titleContentAdd.setContent(content);
                    titleContentAdd.setArtist(artist);
                    titleContentAddList.add(titleContentAdd);
                }
            }
            titleContentAddRepository.saveAll(titleContentAddList);
        }
    }

    private void updateYearContentAdds(Artist artist, List<String> yearContentAdds) {
        if (yearContentAdds != null) {
            List<YearContentAdd> yearContentAddList = new ArrayList<>();
            for (String content : yearContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    YearContentAdd yearContentAdd = new YearContentAdd();
                    yearContentAdd.setContent(content);
                    yearContentAdd.setArtist(artist);
                    yearContentAddList.add(yearContentAdd);
                }
            }
            yearContentAddRepository.saveAll(yearContentAddList);
        }
    }

    private void updateWidthContentAdds(Artist artist, List<String> widthContentAdds) {
        if (widthContentAdds != null) {
            List<WidthContentAdd> widthContentAddList = new ArrayList<>();
            for (String content : widthContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    WidthContentAdd widthContentAdd = new WidthContentAdd();
                    widthContentAdd.setContent(content);
                    widthContentAdd.setArtist(artist);
                    widthContentAddList.add(widthContentAdd);
                }
            }
            widthContentAddRepository.saveAll(widthContentAddList);
        }
    }

    private void updateHeightContentAdds(Artist artist, List<String> heightContentAdds) {
        if (heightContentAdds != null) {
            List<HeightContentAdd> heightContentAddList = new ArrayList<>();
            for (String content : heightContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    HeightContentAdd heightContentAdd = new HeightContentAdd();
                    heightContentAdd.setContent(content);
                    heightContentAdd.setArtist(artist);
                    heightContentAddList.add(heightContentAdd);
                }
            }
            heightContentAddRepository.saveAll(heightContentAddList);
        }
    }

    private void updateUnitContentAdds(Artist artist, List<String> unitContentAdds) {
        if (unitContentAdds != null) {
            List<UnitContentAdd> unitContentAddList = new ArrayList<>();
            for (String content : unitContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    UnitContentAdd unitContentAdd = new UnitContentAdd();
                    unitContentAdd.setContent(content);
                    unitContentAdd.setArtist(artist);
                    unitContentAddList.add(unitContentAdd);
                }
            }
            unitContentAddRepository.saveAll(unitContentAddList);
        }
    }

    private void updateTechniqueContentAdds(Artist artist, List<String> techniqueContentAdds) {
        if (techniqueContentAdds != null) {
            List<TechniqueContentAdd> techniqueContentAddList = new ArrayList<>();
            for (String content : techniqueContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    TechniqueContentAdd techniqueContentAdd = new TechniqueContentAdd();
                    techniqueContentAdd.setContent(content);
                    techniqueContentAdd.setArtist(artist);
                    techniqueContentAddList.add(techniqueContentAdd);
                }
            }
            techniqueContentAddRepository.saveAll(techniqueContentAddList);
        }
    }

    public void delete(Artist artist) {
        artistRepository.delete(artist);
    }

    public void saveArtistAdds(List<ArtistAdd> artistAddList) {
        artistAddRepository.saveAll(artistAddList);
    }

    public Artist findByMember(Member member) {
        return artistRepository.findByAuthor(member)
                .orElseThrow(() -> new RuntimeException("해당 회원의 아티스트 정보를 찾을 수 없습니다."));
    }

    public List<Artist> findByKeyword(String keyword) {
        return artistRepository.findByKeyword(keyword);
    }

    public Artist getArtistByKorName(String korName) {
        return artistRepository.findByKorName(korName)
                .orElseThrow(() -> new DataNotFoundException("Artist not found with korName: " + korName));
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
}

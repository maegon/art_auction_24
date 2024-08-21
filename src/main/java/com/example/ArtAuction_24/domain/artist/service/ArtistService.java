package com.example.ArtAuction_24.domain.artist.service;

import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.repository.*;
import com.example.ArtAuction_24.global.DataNotFoundException;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
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

    public Artist create(MultipartFile thumbnail, String korName, String engName, String birthDate, Member member, List<String> artistAdds) {
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
                .author(member)
                .build();
        artistRepository.save(artist);

        // artistAdds만 추가
        addArtistAdditionalInfo(artist, artistAdds);

        return artist;
    }

    private void addArtistAdditionalInfo(Artist artist, List<String> artistAdds) {
        if (artistAdds != null && !artistAdds.isEmpty()) {
            List<ArtistAdd> artistAddList = artistAdds.stream()
                    .filter(add -> add != null && !add.trim().isEmpty()) // 빈 문자열 필터링
                    .map(add -> {
                        ArtistAdd artistAdd = new ArtistAdd();
                        artistAdd.setArtist(artist);
                        artistAdd.setContent(add);
                        return artistAdd;
                    })
                    .collect(Collectors.toList());
            artistAddRepository.saveAll(artistAddList);
        }
    }

    public Artist getArtist(Integer id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("artist not found"));
    }

    @Transactional
    public void modify(Artist artist, MultipartFile thumbnail, String korName, String engName, String birthDate, String introduce,
                       List<String> artistAdds, List<String> titleAdds, List<String> contentAdds,
                       List<String> titleContentAdds, List<String> yearContentAdds,
                       List<String> widthContentAdds, List<String> heightContentAdds,
                       List<String> unitContentAdds, List<String> techniqueContentAdds) {

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
        artist.setIntroduce(introduce);

        // 각 리스트들을 처리하고 저장
        updateArtistAdds(artist, convertToArtistAddList(artistAdds, artist));
        updateTitleAdds(artist, convertToTitleAddList(titleAdds, artist));
        updateContentAdds(artist, convertToContentAddList(contentAdds, artist));
        updateTitleContentAdds(artist, convertToTitleContentAddList(titleContentAdds, artist));
        updateYearContentAdds(artist, convertToYearContentAddList(yearContentAdds, artist));
        updateWidthContentAdds(artist, convertToWidthContentAddList(widthContentAdds, artist));
        updateHeightContentAdds(artist, convertToHeightContentAddList(heightContentAdds, artist));
        updateUnitContentAdds(artist, convertToUnitContentAddList(unitContentAdds, artist));
        updateTechniqueContentAdds(artist, convertToTechniqueContentAddList(techniqueContentAdds, artist));


        // 그룹화된 데이터를 각각 저장
        for (int i = 0; i < titleContentAdds.size(); i++) {
            TitleContentAdd titleContentAdd = new TitleContentAdd();
            titleContentAdd.setContent(titleContentAdds.get(i));
            titleContentAdd.setArtist(artist);

            // 로그 추가
            System.out.println("Saving TitleContentAdd: " + titleContentAdd.getContent());

            titleContentAddRepository.save(titleContentAdd);

            YearContentAdd yearContentAdd = new YearContentAdd();
            yearContentAdd.setContent(yearContentAdds.get(i));
            yearContentAdd.setArtist(artist);

            WidthContentAdd widthContentAdd = new WidthContentAdd();
            widthContentAdd.setContent(widthContentAdds.get(i));
            widthContentAdd.setArtist(artist);

            HeightContentAdd heightContentAdd = new HeightContentAdd();
            heightContentAdd.setContent(heightContentAdds.get(i));
            heightContentAdd.setArtist(artist);

            UnitContentAdd unitContentAdd = new UnitContentAdd();
            unitContentAdd.setContent(unitContentAdds.get(i));
            unitContentAdd.setArtist(artist);

            TechniqueContentAdd techniqueContentAdd = new TechniqueContentAdd();
            techniqueContentAdd.setContent(techniqueContentAdds.get(i));
            techniqueContentAdd.setArtist(artist);

            // 각각의 엔티티를 저장
            titleContentAddRepository.save(titleContentAdd);
            yearContentAddRepository.save(yearContentAdd);
            widthContentAddRepository.save(widthContentAdd);
            heightContentAddRepository.save(heightContentAdd);
            unitContentAddRepository.save(unitContentAdd);
            techniqueContentAddRepository.save(techniqueContentAdd);
        }

        artistRepository.save(artist);
    }

    // 각 리스트를 변환하는 유틸리티 메서드
    private List<ArtistAdd> convertToArtistAddList(List<String> artistAdds, Artist artist) {
        return artistAdds.stream().map(content -> {
            ArtistAdd artistAdd = new ArtistAdd();
            artistAdd.setContent(content);
            artistAdd.setArtist(artist);
            return artistAdd;
        }).collect(Collectors.toList());
    }

    private List<TitleAdd> convertToTitleAddList(List<String> titleAdds, Artist artist) {
        return titleAdds.stream().map(content -> {
            TitleAdd titleAdd = new TitleAdd();
            titleAdd.setContent(content);
            titleAdd.setArtist(artist);
            return titleAdd;
        }).collect(Collectors.toList());
    }

    private List<ContentAdd> convertToContentAddList(List<String> contentAdds, Artist artist) {
        return contentAdds.stream().map(content -> {
            ContentAdd contentAdd = new ContentAdd();
            contentAdd.setContent(content);
            contentAdd.setArtist(artist);
            return contentAdd;
        }).collect(Collectors.toList());
    }

    private List<TitleContentAdd> convertToTitleContentAddList(List<String> titleContentAdds, Artist artist) {
        return titleContentAdds.stream().map(content -> {
            TitleContentAdd titleContentAdd = new TitleContentAdd();
            titleContentAdd.setContent(content);
            titleContentAdd.setArtist(artist);
            return titleContentAdd;
        }).collect(Collectors.toList());
    }

    private List<YearContentAdd> convertToYearContentAddList(List<String> yearContentAdds, Artist artist) {
        return yearContentAdds.stream().map(content -> {
            YearContentAdd yearContentAdd = new YearContentAdd();
            yearContentAdd.setContent(content);
            yearContentAdd.setArtist(artist);
            return yearContentAdd;
        }).collect(Collectors.toList());
    }

    private List<WidthContentAdd> convertToWidthContentAddList(List<String> widthContentAdds, Artist artist) {
        return widthContentAdds.stream().map(content -> {
            WidthContentAdd widthContentAdd = new WidthContentAdd();
            widthContentAdd.setContent(content);
            widthContentAdd.setArtist(artist);
            return widthContentAdd;
        }).collect(Collectors.toList());
    }

    private List<HeightContentAdd> convertToHeightContentAddList(List<String> heightContentAdds, Artist artist) {
        return heightContentAdds.stream().map(content -> {
            HeightContentAdd heightContentAdd = new HeightContentAdd();
            heightContentAdd.setContent(content);
            heightContentAdd.setArtist(artist);
            return heightContentAdd;
        }).collect(Collectors.toList());
    }

    private List<UnitContentAdd> convertToUnitContentAddList(List<String> unitContentAdds, Artist artist) {
        return unitContentAdds.stream().map(content -> {
            UnitContentAdd unitContentAdd = new UnitContentAdd();
            unitContentAdd.setContent(content);
            unitContentAdd.setArtist(artist);
            return unitContentAdd;
        }).collect(Collectors.toList());
    }

    private List<TechniqueContentAdd> convertToTechniqueContentAddList(List<String> techniqueContentAdds, Artist artist) {
        return techniqueContentAdds.stream().map(content -> {
            TechniqueContentAdd techniqueContentAdd = new TechniqueContentAdd();
            techniqueContentAdd.setContent(content);
            techniqueContentAdd.setArtist(artist);
            return techniqueContentAdd;
        }).collect(Collectors.toList());
    }



    private void updateArtistAdds(Artist artist, List<ArtistAdd> artistAdds) {
        if (artistAdds != null) {
            artistAddRepository.deleteAllByArtist(artist); // 기존 데이터 삭제
            artistAdds.forEach(add -> add.setArtist(artist)); // 새 데이터에 아티스트 연결
            artistAddRepository.saveAll(artistAdds); // 새 데이터 저장
        }
    }

    private void updateTitleAdds(Artist artist, List<TitleAdd> titleAdds) {
        if (titleAdds != null) {
            titleAddRepository.deleteAllByArtist(artist);
            titleAdds.forEach(add -> add.setArtist(artist));
            titleAddRepository.saveAll(titleAdds);
        }
    }

    private void updateContentAdds(Artist artist, List<ContentAdd> contentAdds) {
        if (contentAdds != null) {
            contentAddRepository.deleteAllByArtist(artist);
            contentAdds.forEach(add -> add.setArtist(artist));
            contentAddRepository.saveAll(contentAdds);
        }
    }

    private void updateTitleContentAdds(Artist artist, List<TitleContentAdd> titleContentAdds) {
        if (titleContentAdds != null) {
            titleContentAddRepository.deleteAllByArtist(artist);
            titleContentAdds.forEach(add -> add.setArtist(artist));
            titleContentAddRepository.saveAll(titleContentAdds);
        }
    }

    private void updateYearContentAdds(Artist artist, List<YearContentAdd> yearContentAdds) {
        if (yearContentAdds != null) {
            yearContentAddRepository.deleteAllByArtist(artist);
            yearContentAdds.forEach(add -> add.setArtist(artist));
            yearContentAddRepository.saveAll(yearContentAdds);
        }
    }

    private void updateWidthContentAdds(Artist artist, List<WidthContentAdd> widthContentAdds) {
        if (widthContentAdds != null) {
            widthContentAddRepository.deleteAllByArtist(artist);
            widthContentAdds.forEach(add -> add.setArtist(artist));
            widthContentAddRepository.saveAll(widthContentAdds);
        }
    }

    private void updateHeightContentAdds(Artist artist, List<HeightContentAdd> heightContentAdds) {
        if (heightContentAdds != null) {
            heightContentAddRepository.deleteAllByArtist(artist);
            heightContentAdds.forEach(add -> add.setArtist(artist));
            heightContentAddRepository.saveAll(heightContentAdds);
        }
    }

    private void updateUnitContentAdds(Artist artist, List<UnitContentAdd> unitContentAdds) {
        if (unitContentAdds != null) {
            unitContentAddRepository.deleteAllByArtist(artist);
            unitContentAdds.forEach(add -> add.setArtist(artist));
            unitContentAddRepository.saveAll(unitContentAdds);
        }
    }

    private void updateTechniqueContentAdds(Artist artist, List<TechniqueContentAdd> techniqueContentAdds) {
        if (techniqueContentAdds != null) {
            techniqueContentAddRepository.deleteAllByArtist(artist);
            techniqueContentAdds.forEach(add -> add.setArtist(artist));
            techniqueContentAddRepository.saveAll(techniqueContentAdds);
        }
    }

    @Transactional
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
        List<Artist> artists = artistRepository.findAllByKorName(korName);
        if (artists.size() > 1) {
            return artists.get(0); // 중복이 있을 경우 첫 번째 결과를 반환
        } else if (artists.size() == 1) {
            return artists.get(0);
        } else {
            throw new DataNotFoundException("Artist not found with korName: " + korName);
        }
    }

    public Artist Create(String korName, String engName, String birthDate, String introduce) {
        Artist artist = Artist.builder()
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .introduce(introduce)
                .build();
        artistRepository.save(artist);

        return artist;
    }

    public void updateArtistDetails(Artist artist, ArtistForm artistForm) {
        // 기본 정보 업데이트
        artist.setKorName(artistForm.getKorName());
        artist.setEngName(artistForm.getEngName());
        artist.setBirthDate(artistForm.getBirthDate());
        artist.setIntroduce(artistForm.getIntroduce());

        // 추가 필드 업데이트
        updateArtistAdds(artist, convertToArtistAddList(artistForm.getArtistAdds(), artist));
        updateTitleAdds(artist, convertToTitleAddList(artistForm.getTitleAdds(), artist));
        updateContentAdds(artist, convertToContentAddList(artistForm.getContentAdds(), artist));
        updateTitleContentAdds(artist, convertToTitleContentAddList(artistForm.getTitleContentAdds(), artist));
        updateYearContentAdds(artist, convertToYearContentAddList(artistForm.getYearContentAdds(), artist));
        updateWidthContentAdds(artist, convertToWidthContentAddList(artistForm.getWidthContentAdds(), artist));
        updateHeightContentAdds(artist, convertToHeightContentAddList(artistForm.getHeightContentAdds(), artist));
        updateUnitContentAdds(artist, convertToUnitContentAddList(artistForm.getUnitContentAdds(), artist));
        updateTechniqueContentAdds(artist, convertToTechniqueContentAddList(artistForm.getTechniqueContentAdds(), artist));

        // 변경된 아티스트 엔티티 저장
        artistRepository.save(artist);
    }

}
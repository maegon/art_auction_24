package com.example.ArtAuction_24.domain.artist.service;

import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.repository.*;
import com.example.ArtAuction_24.global.DataNotFoundException;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final ArtistAddRepository artistAddRepository; // 추가
    private final TitleAddRepository titleAddRepository;  // 추가
    private final ContentAddRepository contentAddRepository;  // 추가
    private final IntroduceContentAddRepository introduceContentAddRepository;
    private final MajorWorkContentAddRepository majorWorkContentAddRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public Artist create(MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType, Member member) {
        String thumbnailRelPath = "image/artist/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        // 파일 저장 전 디렉토리 존재 확인 및 생성
        File dir = new File(fileDirPath + "/image/artist");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("디렉토리 생성 실패: " + dir.getAbsolutePath());
            }
        }

        try {
            thumbnail.transferTo(thumbnailFile);
            System.out.println("파일 저장 경로: " + thumbnailFile.getAbsolutePath());
            // 파일 저장 후 확인
            if (!thumbnailFile.exists()) {
                throw new RuntimeException("파일 저장 실패: " + thumbnailFile.getAbsolutePath());
            } else {
                System.out.println("파일 저장 성공: " + thumbnailFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("파일 저장 중 예외 발생: " + e.getMessage());
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
        Optional<Artist> of = artistRepository.findById(id);
        if (of.isEmpty()) throw new DataNotFoundException("artist not found");
        return of.get();
    }



    public void modify(Artist artist, MultipartFile thumbnail, String korName, String engName, String birthDate, String tel, String mail, String mailType,
                       List<String> artistAdds, List<String> titleAdds, List<String> contentAdds,
                       List<String> introduceContentAdds, List<String> majorWorkContentAdds) {

        // 이미지 파일 업데이트 처리
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

        // 기본 정보 업데이트
        artist.setKorName(korName);
        artist.setEngName(engName);
        artist.setBirthDate(birthDate);
        artist.setTel(tel);
        artist.setMail(mail);
        artist.setMailType(mailType);

        // 연관된 엔티티 업데이트
        updateArtistAdds(artist, artistAdds);
        updateTitleAdds(artist, titleAdds);
        updateContentAdds(artist, contentAdds);
        updateIntroduceContentAdds(artist, introduceContentAdds);
        updateMajorWorkContentAdds(artist, majorWorkContentAdds);

        // 엔티티 저장
        artistRepository.save(artist);
    }

    private void updateArtistAdds(Artist artist, List<String> artistAdds) {
        // 새 ArtistAdd 추가
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
        // 새 TitleAdd 추가
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
        // 새 ContentAdd 추가
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

    private void updateIntroduceContentAdds(Artist artist, List<String> introduceContentAdds) {
        // 새 IntroduceContentAdd 추가
        if (introduceContentAdds != null) {
            List<IntroduceContentAdd> introduceContentAddList = new ArrayList<>();
            for (String content : introduceContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    IntroduceContentAdd introduceContentAdd = new IntroduceContentAdd();
                    introduceContentAdd.setContent(content);
                    introduceContentAdd.setArtist(artist);
                    introduceContentAddList.add(introduceContentAdd);
                }
            }
            introduceContentAddRepository.saveAll(introduceContentAddList);
        }
    }

    private void updateMajorWorkContentAdds(Artist artist, List<String> majorWorkContentAdds) {
        // 새 MajorWorkContentAdd 추가
        if (majorWorkContentAdds != null) {
            List<MajorWorkContentAdd> majorWorkContentAddList = new ArrayList<>();
            for (String content : majorWorkContentAdds) {
                if (content != null && !content.trim().isEmpty()) {
                    MajorWorkContentAdd majorWorkContentAdd = new MajorWorkContentAdd();
                    majorWorkContentAdd.setContent(content);
                    majorWorkContentAdd.setArtist(artist);
                    majorWorkContentAddList.add(majorWorkContentAdd);
                }
            }
            majorWorkContentAddRepository.saveAll(majorWorkContentAddList);
        }
    }


    public void delete(Artist artist) {
        artistRepository.delete(artist);
    }

    public void saveArtistAdds(List<ArtistAdd> artistAddList) {
        artistAddRepository.saveAll(artistAddList); // ArtistAdd 객체 리스트 저장
    }

    public Artist findByMember(Member member) {
        return artistRepository.findByAuthor(member)
                .orElseThrow(() -> new RuntimeException("해당 회원의 아티스트 정보를 찾을 수 없습니다."));
    }

    public List<Artist> findByKeyword(String keyword) {
        return artistRepository.findByKeyword(keyword);
    }

    public Artist getArtistByKorName(String korName) {
        Optional<Artist> artistOptional = artistRepository.findByKorName(korName);
        return artistOptional.orElseThrow(() -> new DataNotFoundException("Artist not found with korName: " + korName));
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
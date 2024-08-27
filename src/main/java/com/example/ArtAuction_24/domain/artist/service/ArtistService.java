package com.example.ArtAuction_24.domain.artist.service;

import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.repository.*;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import com.example.ArtAuction_24.domain.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;
    private final ArtistAddRepository artistAddRepository;
    private final TitleAddRepository titleAddRepository;
    private final ContentAddRepository contentAddRepository;

    private final TitleContentAddRepository titleContentAddRepository;

    private final YearContentAddRepository yearContentAddRepository;

    private final WidthContentAddRepository widthContentAddRepository;

    private final HeightContentAddRepository heightContentAddRepository;

    private final UnitContentAddRepository unitContentAddRepository;
    @Autowired
    private final TechniqueContentAddRepository techniqueContentAddRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public void uploadProofFile(Member member, MultipartFile proofFile) {
        String proofRelPath = "pdf/proofs/" + member.getId() + "__" + proofFile.getOriginalFilename();
        File proofFilePath = new File(fileDirPath + "/" + proofRelPath);

        File dir = new File(fileDirPath + "/pdf/proofs");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("디렉토리 생성 실패: " + dir.getAbsolutePath());
            }
        }

        try {
            proofFile.transferTo(proofFilePath);
            if (!proofFilePath.exists()) {
                throw new RuntimeException("파일 저장 실패: " + proofFilePath.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
        }

        member.setProofFilePath(proofRelPath);
        member.setProofSubmitted(true);
        memberRepository.save(member);
    }

    public List<Member> getMembersPendingApproval() {
        return memberRepository.findByProofSubmittedTrueAndApprovedArtistFalse();
    }

    // 승인된 멤버들을 가져오는 메서드
    public List<Member> getMembersApproved() {
        return memberRepository.findByProofSubmittedTrueAndApprovedArtistTrue();
    }


    @Transactional
    public void approveMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException("Member not found"));
        member.setApprovedArtist(true);
        memberRepository.save(member);
    }

    @Transactional
    public void rejectMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException("Member not found"));
        member.setProofFilePath(null);
        member.setProofSubmitted(false);
        memberRepository.save(member);
    }
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

        // Artist 객체 생성 및 저장
        Artist artist = Artist.builder()
                .thumbnailImg(thumbnailRelPath)
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .author(member)
                .balance(0L)
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

    public Artist create(String korName, String engName, String birthDate, String introduce, String majorWork, Member member) {
        Artist artist = Artist.builder()
                .korName(korName)
                .engName(engName)
                .birthDate(birthDate)
                .introduce(introduce)
                .majorWork(majorWork)
                .balance(0L)
                .author(member)
                .build();
        artistRepository.save(artist);

        return artist;
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
        updateOrAddTitleContentAdds(artist, convertToTitleContentAddList(titleContentAdds, artist), convertToYearContentAddList(yearContentAdds, artist),
                convertToWidthContentAddList(widthContentAdds, artist), convertToHeightContentAddList(heightContentAdds, artist),
                convertToUnitContentAddList(unitContentAdds, artist), convertToTechniqueContentAddList(techniqueContentAdds, artist));

        // 중복 항목 제거
        removeDuplicateEntries(artist);
        artistRepository.flush();  // 변경 사항 반영

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

    // 기존 데이터와 새로운 데이터를 비교하여 업데이트 또는 추가하는 메서드들
    private void updateArtistAdds(Artist artist, List<ArtistAdd> newArtistAdds) {
        artistAddRepository.deleteAllByArtist(artist); // 기존 데이터 삭제
        newArtistAdds.forEach(add -> add.setArtist(artist)); // 새 데이터에 아티스트 연결
        artistAddRepository.saveAll(newArtistAdds); // 새 데이터 저장
    }

    private void updateTitleAdds(Artist artist, List<TitleAdd> newTitleAdds) {
        titleAddRepository.deleteAllByArtist(artist);
        newTitleAdds.forEach(add -> add.setArtist(artist));
        titleAddRepository.saveAll(newTitleAdds);
    }

    private void updateContentAdds(Artist artist, List<ContentAdd> newContentAdds) {
        contentAddRepository.deleteAllByArtist(artist);
        newContentAdds.forEach(add -> add.setArtist(artist));
        contentAddRepository.saveAll(newContentAdds);
    }

    private void updateOrAddTitleContentAdds(Artist artist, List<TitleContentAdd> newTitleContentAdds, List<YearContentAdd> newYearContentAdds,
                                             List<WidthContentAdd> newWidthContentAdds, List<HeightContentAdd> newHeightContentAdds,
                                             List<UnitContentAdd> newUnitContentAdds, List<TechniqueContentAdd> newTechniqueContentAdds) {
        // 그룹화된 데이터 업데이트 로직
        for (int i = 0; i < newTitleContentAdds.size(); i++) {
            TitleContentAdd newTitleContentAdd = newTitleContentAdds.get(i);
            YearContentAdd newYearContentAdd = newYearContentAdds.get(i);
            WidthContentAdd newWidthContentAdd = newWidthContentAdds.get(i);
            HeightContentAdd newHeightContentAdd = newHeightContentAdds.get(i);
            UnitContentAdd newUnitContentAdd = newUnitContentAdds.get(i);
            TechniqueContentAdd newTechniqueContentAdd = newTechniqueContentAdds.get(i);

            // 중복된 데이터를 찾기 위해 현재 데이터베이스의 항목을 탐색합니다.
            boolean exists = false;
            for (TitleContentAdd existingTitleContentAdd : artist.getTitleContentAdds()) {
                if (existingTitleContentAdd.getContent().equals(newTitleContentAdd.getContent())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                // 새로운 항목을 추가합니다.
                artist.getTitleContentAdds().add(newTitleContentAdd);
                artist.getYearContentAdds().add(newYearContentAdd);
                artist.getWidthContentAdds().add(newWidthContentAdd);
                artist.getHeightContentAdds().add(newHeightContentAdd);
                artist.getUnitContentAdds().add(newUnitContentAdd);
                artist.getTechniqueContentAdds().add(newTechniqueContentAdd);
            }
        }

        // 기존 데이터를 삭제
        deleteOldEntries(artist.getTitleContentAdds(), newTitleContentAdds, titleContentAddRepository);
        deleteOldEntries(artist.getYearContentAdds(), newYearContentAdds, yearContentAddRepository);
        deleteOldEntries(artist.getWidthContentAdds(), newWidthContentAdds, widthContentAddRepository);
        deleteOldEntries(artist.getHeightContentAdds(), newHeightContentAdds, heightContentAddRepository);
        deleteOldEntries(artist.getUnitContentAdds(), newUnitContentAdds, unitContentAddRepository);
        deleteOldEntries(artist.getTechniqueContentAdds(), newTechniqueContentAdds, techniqueContentAddRepository);
    }

    private <T extends ContentAddBase> void deleteOldEntries(List<T> currentEntries, List<T> newEntries, JpaRepository<T, Long> repository) {
        Set<String> newContentSet = new HashSet<>();
        for (T entry : newEntries) {
            newContentSet.add(entry.getContent());
        }

        List<T> entriesToDelete = new ArrayList<>();
        for (T entry : currentEntries) {
            if (!newContentSet.contains(entry.getContent())) {
                entriesToDelete.add(entry);
            }
        }

        repository.deleteAll(entriesToDelete);
    }

    // 중복 항목 제거 메소드
    public void removeDuplicateEntries(Artist artist) {
        // TitleContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getTitleContentAdds(), titleContentAddRepository);

        // YearContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getYearContentAdds(), yearContentAddRepository);

        // WidthContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getWidthContentAdds(), widthContentAddRepository);

        // HeightContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getHeightContentAdds(), heightContentAddRepository);

        // UnitContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getUnitContentAdds(), unitContentAddRepository);

        // TechniqueContentAdd 중복 제거
        removeDuplicatesForContentAdd(artist.getTechniqueContentAdds(), techniqueContentAddRepository);
    }

    // 중복된 항목을 제거하는 공통 메서드
    private <T extends ContentAddBase> void removeDuplicatesForContentAdd(List<T> contentAdds, JpaRepository<T, Long> repository) {
        Set<String> uniqueContent = new HashSet<>();
        List<T> duplicates = new ArrayList<>();

        for (T contentAdd : contentAdds) {
            if (!uniqueContent.add(contentAdd.getContent())) {
                duplicates.add(contentAdd);
            }
        }

        repository.deleteAll(duplicates);
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
                .orElseThrow(() -> new RuntimeException("해당 회원의 아티스트 정보를 찾을 수 없습니다. 뒤로가기를 눌러 작가 프로필을 만드세요"));
    }

    public List<Artist> findByKeyword(String keyword) {
        return artistRepository.findByKeyword(keyword);
    }

    public Artist getArtistByKorName(String korName) {
        List<Artist> artists = artistRepository.findAllByKorName(korName);
        if (artists.size() > 1) {
            // 첫 번째 결과를 반환하거나 로그를 남기고 중복을 처리
            return artists.get(0);
        } else if (artists.size() == 1) {
            return artists.get(0);
        } else {
            throw new DataNotFoundException("Artist not found with korName: " + korName);
        }
    }

    public Artist create(String korName, String engName, String birthDate, String introduce) {
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
        updateOrAddTitleContentAdds(artist, convertToTitleContentAddList(artistForm.getTitleContentAdds(), artist),
                convertToYearContentAddList(artistForm.getYearContentAdds(), artist), convertToWidthContentAddList(artistForm.getWidthContentAdds(), artist),
                convertToHeightContentAddList(artistForm.getHeightContentAdds(), artist), convertToUnitContentAddList(artistForm.getUnitContentAdds(), artist),
                convertToTechniqueContentAddList(artistForm.getTechniqueContentAdds(), artist));

        // 변경된 아티스트 엔티티 저장
        artistRepository.save(artist);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public void favoriteArtist(Member member, Integer artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + artistId));
        member.getFavoriteArtists().add(artist);
        memberRepository.save(member);
    }

    public boolean toggleFavoriteArtist(Member member, Integer artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        if (member.getFavoriteArtists().contains(artist)) {
            member.getFavoriteArtists().remove(artist); // 제거
            memberRepository.save(member);
            return false; // 제거됨
        } else {
            member.getFavoriteArtists().add(artist); // 추가
            memberRepository.save(member);
            return true; // 추가됨
        }
    }

    public Artist getArtistById(Long artistId) {
        return artistRepository.findById(Math.toIntExact(artistId))
                .orElseThrow(() -> new RuntimeException("Artist not found with ID: " + artistId));
    }

    public Optional<Artist> getArtistByMember(Member member) {
        return artistRepository.findByAuthor(member);
    }

}

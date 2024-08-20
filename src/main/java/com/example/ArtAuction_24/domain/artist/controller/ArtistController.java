package com.example.ArtAuction_24.domain.artist.controller;

import com.example.ArtAuction_24.domain.art.service.ArtService;
import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/artist")
@Controller
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final MemberService memberService;
    private final ArtService artService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public String getProfile(Model model, @PathVariable("id") Integer id) {
        Artist artist = artistService.getArtist(id);
        Member currentMember = memberService.getCurrentMember();
        System.out.println("Current Member: " + currentMember);
        // 현재 로그인한 사용자의 이메일과 전화번호를 모델에 추가합니다.
        model.addAttribute("email", currentMember.getEmail());
        model.addAttribute("phoneNumber", currentMember.getPhoneNumber());
        Optional<Artist> artistOpt = Optional.ofNullable(artistService.findByMember(currentMember));

        if (artistOpt.isEmpty()) {
            System.out.println("아티스트 정보를 찾을 수 없습니다.");
            model.addAttribute("errorMessage", "아티스트 정보를 찾을 수 없습니다.");
            return "error/artistNotFound";
        }

        System.out.println("Artist: " + artistOpt.get());
        model.addAttribute("artist", artistOpt.get());
        return "artist/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/terms")
    public String showTermsForm(Model model) {
        // 약관 동의 폼을 위한 모델 추가
        return "artist/termsForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/terms")
    public String submitTerms(
            @RequestParam(name = "agree_personal_info", required = false, defaultValue = "false") boolean agreePersonalInfo,
            @RequestParam(name = "agree_service", required = false, defaultValue = "false") boolean agreeService,
            @RequestParam(name = "agree_age", required = false, defaultValue = "false") boolean agreeAge,
            @RequestParam(name = "agree_location", required = false) Boolean agreeLocation, // 선택적 항목
            Model model) {

        // 필수 항목이 동의되지 않은 경우
        if (!agreePersonalInfo || !agreeService || !agreeAge) {
            model.addAttribute("errorMessage", "필수 항목에 동의하셔야 합니다.");
            return "artist/termsForm";
        }

        // 모든 필수 항목이 동의된 경우
        return "redirect:/artist/create";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("artistForm", new ArtistForm());
        return "artist/artistForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(
            @ModelAttribute @Valid ArtistForm artistForm,
            BindingResult bindingResult,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam(value = "artistAdds", required = false) List<String> artistAdds,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            return "artist/artistForm";
        }

        if (thumbnail.isEmpty()) {
            System.out.println("파일이 업로드되지 않았습니다.");
            return "artist/artistForm";
        } else {
            System.out.println("파일 업로드 성공: " + thumbnail.getOriginalFilename());
        }

        Member member = this.memberService.getCurrentMember();
        Artist artist = this.artistService.create(
                thumbnail,
                artistForm.getKorName(),
                artistForm.getEngName(),
                artistForm.getBirthDate(),
                member
        );

        if (artist == null) {
            System.out.println("DB에 아티스트 정보가 저장되지 않았습니다.");
            return "artist/artistForm";
        }

        if (artistAdds == null) {
            artistAdds = new ArrayList<>();
        }

        List<ArtistAdd> artistAddList = new ArrayList<>();
        for (String content : artistAdds) {
            if (content != null && !content.trim().isEmpty()) {
                ArtistAdd artistAdd = new ArtistAdd();
                artistAdd.setContent(content);
                artistAdd.setArtist(artist);
                artistAddList.add(artistAdd);
            }
        }
        this.artistService.saveArtistAdds(artistAddList);

        return "redirect:/artist/profile/" + artist.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String artistModify(@ModelAttribute @Valid ArtistForm artistForm,
                               @PathVariable("id") Integer id,
                               BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {

            return "artist/profileForm";
        }

        Artist artist = artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        artistService.modify(
                artist,
                artistForm.getThumbnail(),
                artistForm.getKorName(),
                artistForm.getEngName(),
                artistForm.getBirthDate(),
                artistForm.getIntroduce(),
                artistForm.getArtistAdds(),
                artistForm.getTitleAdds(),
                artistForm.getContentAdds(),
                artistForm.getTitleContentAdds(),
                artistForm.getYearContentAdds(),
                artistForm.getWidthContentAdds(),
                artistForm.getHeightContentAdds(),
                artistForm.getUnitContentAdds(),
                artistForm.getTechniqueContentAdds()
        );

        System.out.println("getThumbnail_1:" + artistForm.getThumbnail());
        System.out.println("getKorName_1:" + artistForm.getKorName());
        System.out.println("getEngName_1:" + artistForm.getEngName());
        System.out.println("getBirthDate_1:" + artistForm.getBirthDate());
        System.out.println("getIntroduce_1:" + artistForm.getIntroduce());
        System.out.println("getArtistAdds_1:" + artistForm.getArtistAdds());
        System.out.println("getTitleAdds_1:" + artistForm.getTitleAdds());
        System.out.println("getContentAdds_1:" + artistForm.getContentAdds());
        System.out.println("getTitleContentAdds_1:" + artistForm.getTitleContentAdds());
        System.out.println("getYearContentAdds_1:" + artistForm.getYearContentAdds());
        System.out.println("getWidthContentAdds_1:" + artistForm.getWidthContentAdds());
        System.out.println("getHeightContentAdds_1:" + artistForm.getHeightContentAdds());
        System.out.println("getUnitContentAdds_1:" + artistForm.getUnitContentAdds());
        System.out.println("getTechniqueContentAdds_1:" + artistForm.getTechniqueContentAdds());

        return "redirect:/artist/profile/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String artistModify(Model model, @PathVariable("id") Integer id, Principal principal) {
        Artist artist = artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        ArtistForm artistForm = new ArtistForm();
        artistForm.setKorName(artist.getKorName());
        artistForm.setEngName(artist.getEngName());
        artistForm.setBirthDate(artist.getBirthDate());
        artistForm.setIntroduce(artist.getIntroduce());
        artistForm.setExistingThumbnailUrl(artist.getThumbnailImg());

        // 기존의 관련 데이터 설정
        artistForm.setArtistAdds(artist.getArtistAdds().stream()
                .map(ArtistAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTitleAdds(artist.getTitleAdds().stream()
                .map(TitleAdd::getContent)  // `getTitle()` 메서드가 `getContent()`로 수정됨을 주의하세요
                .collect(Collectors.toList()));
        artistForm.setContentAdds(artist.getContentAdds().stream()
                .map(ContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTitleContentAdds(artist.getTitleContentAdds().stream()
                .map(TitleContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setYearContentAdds(artist.getYearContentAdds().stream()
                .map(YearContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setWidthContentAdds(artist.getWidthContentAdds().stream()
                .map(WidthContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setHeightContentAdds(artist.getHeightContentAdds().stream()
                .map(HeightContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setUnitContentAdds(artist.getUnitContentAdds().stream()
                .map(UnitContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTechniqueContentAdds(artist.getTechniqueContentAdds().stream()
                .map(TechniqueContentAdd::getContent)
                .collect(Collectors.toList()));

        System.out.println("getThumbnail:" + artistForm.getThumbnail());
        System.out.println("getKorName:" + artistForm.getKorName());
        System.out.println("getEngName:" + artistForm.getEngName());
        System.out.println("getBirthDate:" + artistForm.getBirthDate());
        System.out.println("getIntroduce:" + artistForm.getIntroduce());
        System.out.println("getArtistAdds:" + artistForm.getArtistAdds());
        System.out.println("getTitleAdds:" + artistForm.getTitleAdds());
        System.out.println("getContentAdds:" + artistForm.getContentAdds());
        System.out.println("getTitleContentAdds:" + artistForm.getTitleContentAdds());
        System.out.println("getYearContentAdds:" + artistForm.getYearContentAdds());
        System.out.println("getWidthContentAdds:" + artistForm.getWidthContentAdds());
        System.out.println("getHeightContentAdds:" + artistForm.getHeightContentAdds());
        System.out.println("getUnitContentAdds:" + artistForm.getUnitContentAdds());
        System.out.println("getTechniqueContentAdds:" + artistForm.getTechniqueContentAdds());

        model.addAttribute("artistForm", artistForm);
        model.addAttribute("artist", artist);

        return "artist/profileForm";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String artistDelete(Principal principal, @PathVariable("id") Integer id) {
        Artist artist = this.artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.artService.deleteAllByArtist(artist);

        // 그 후 작가를 삭제
        this.artistService.delete(artist);

        artistService.delete(artist);

        return "redirect:/";
    }
}

package com.example.ArtAuction_24.domain.artist.controller;

import com.example.ArtAuction_24.domain.art.service.ArtService;
import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;
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
        model.addAttribute("email", currentMember.getEmail());
        model.addAttribute("phoneNumber", currentMember.getPhoneNumber());

        model.addAttribute("artist", artist);
        return "artist/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/terms")
    public String showTermsForm(Model model) {
        return "artist/termsForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/terms")
    public String submitTerms(
            @RequestParam(name = "agree_personal_info", required = false, defaultValue = "false") boolean agreePersonalInfo,
            @RequestParam(name = "agree_service", required = false, defaultValue = "false") boolean agreeService,
            @RequestParam(name = "agree_age", required = false, defaultValue = "false") boolean agreeAge,
            @RequestParam(name = "agree_location", required = false) Boolean agreeLocation,
            Model model) {

        if (!agreePersonalInfo || !agreeService || !agreeAge) {
            model.addAttribute("errorMessage", "필수 항목에 동의하셔야 합니다.");
            return "artist/termsForm";
        }

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
            Principal principal) {

        if (bindingResult.hasErrors()) {
            return "artist/artistForm";
        }

        if (thumbnail.isEmpty()) {
            System.out.println("파일이 업로드되지 않았습니다.");
            return "artist/artistForm";
        }

        Member member = this.memberService.getCurrentMember();

        // Artist를 생성하고, artistAdds를 함께 처리
        Artist artist = this.artistService.create(
                thumbnail,
                artistForm.getKorName(),
                artistForm.getEngName(),
                artistForm.getBirthDate(),
                member,
                artistForm.getArtistAdds()  // 추가된 필드를 함께 전달
        );

        if (artist == null) {
            return "artist/artistForm";
        }

        return "redirect:/artist/profile/" + artist.getId();
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

        // 각종 추가 필드들을 설정
        artistForm.setArtistAdds(Optional.ofNullable(artist.getArtistAdds()).orElse(Collections.emptyList()).stream()
                .map(ArtistAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTitleAdds(Optional.ofNullable(artist.getTitleAdds()).orElse(Collections.emptyList()).stream()
                .map(TitleAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setContentAdds(Optional.ofNullable(artist.getContentAdds()).orElse(Collections.emptyList()).stream()
                .map(ContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTitleContentAdds(Optional.ofNullable(artist.getTitleContentAdds()).orElse(Collections.emptyList()).stream()
                .map(TitleContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setYearContentAdds(Optional.ofNullable(artist.getYearContentAdds()).orElse(Collections.emptyList()).stream()
                .map(YearContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setWidthContentAdds(Optional.ofNullable(artist.getWidthContentAdds()).orElse(Collections.emptyList()).stream()
                .map(WidthContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setHeightContentAdds(Optional.ofNullable(artist.getHeightContentAdds()).orElse(Collections.emptyList()).stream()
                .map(HeightContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setUnitContentAdds(Optional.ofNullable(artist.getUnitContentAdds()).orElse(Collections.emptyList()).stream()
                .map(UnitContentAdd::getContent)
                .collect(Collectors.toList()));
        artistForm.setTechniqueContentAdds(Optional.ofNullable(artist.getTechniqueContentAdds()).orElse(Collections.emptyList()).stream()
                .map(TechniqueContentAdd::getContent)
                .collect(Collectors.toList()));


        model.addAttribute("artistForm", artistForm);
        model.addAttribute("artist", artist);

        return "artist/profileForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String artistModify(
            @ModelAttribute @Valid ArtistForm artistForm,
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

        // 줄바꿈을 기준으로 데이터를 분할하여 List로 변환
        List<String> artistAddsList = artistForm.getArtistAdds();
        List<String> titleAddsList = artistForm.getTitleAdds();
        List<String> contentAddsList = artistForm.getContentAdds();
        List<String> titleContentAddsList = artistForm.getTitleContentAdds();
        List<String> yearContentAddsList = artistForm.getYearContentAdds();
        List<String> widthContentAddsList = artistForm.getWidthContentAdds();
        List<String> heightContentAddsList = artistForm.getHeightContentAdds();
        List<String> unitContentAddsList = artistForm.getUnitContentAdds();
        List<String> techniqueContentAddsList = artistForm.getTechniqueContentAdds();

        // Service 호출
        artistService.modify(
                artist,
                artistForm.getThumbnail(),
                artistForm.getKorName(),
                artistForm.getEngName(),
                artistForm.getBirthDate(),
                artistForm.getIntroduce(),
                artistAddsList,
                titleAddsList,
                contentAddsList,
                titleContentAddsList,
                yearContentAddsList,
                widthContentAddsList,
                heightContentAddsList,
                unitContentAddsList,
                techniqueContentAddsList
        );

        return "redirect:/artist/profile/" + id;
    }

    // 줄바꿈을 기준으로 문자열을 분할하여 List로 변환하는 유틸리티 메서드
    private List<String> convertToList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(input.split("\\r?\\n"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String artistDelete(Principal principal, @PathVariable("id") Integer id) {
        Artist artist = this.artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.artService.deleteAllByArtist(artist);
        this.artistService.delete(artist);

        return "redirect:/";
    }
}

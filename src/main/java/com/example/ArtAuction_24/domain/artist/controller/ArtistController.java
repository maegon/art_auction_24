package com.example.ArtAuction_24.domain.artist.controller;

import com.example.ArtAuction_24.domain.artist.entity.*;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private final ArtistService artistService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public String getProfile(Model model, @PathVariable("id") Integer id) {
        Artist artist = artistService.getArtist(id);
        Member currentMember = memberService.getCurrentMember();

        // 현재 로그인한 사용자의 이메일과 전화번호를 모델에 추가합니다.
        model.addAttribute("email", currentMember.getEmail());
        model.addAttribute("phoneNumber", currentMember.getPhoneNumber());
        model.addAttribute("artist", artist);

        return "artist/profile";
    }

    @PreAuthorize("hasAuthority('ARTIST')")
    @GetMapping("/myProfile")
    public String getMyProfile(Model model) {
        Member currentMember = memberService.getCurrentMember();
        Artist artist = artistService.findByMember(currentMember);

        if (artist == null) {
            // 프로필이 없을 경우
            model.addAttribute("message", "프로필이 없습니다.");
            return "artist/noProfile"; // 'noProfile.html'이라는 뷰 페이지로 이동합니다.
        }

        // 현재 로그인한 사용자의 이메일과 전화번호를 모델에 추가합니다.
        model.addAttribute("email", currentMember.getEmail());
        model.addAttribute("phoneNumber", currentMember.getPhoneNumber());
        model.addAttribute("artist", artist);

        return "redirect:/artist/profile/" + artist.getId();
    }




    @GetMapping("/terms")
    public String showTermsForm(Model model, Principal principal) {
        Member member = memberService.getCurrentMember();

        // 사용자가 이미 승인 요청을 했다면 /uploaded로 리다이렉트
        if (member.isProofSubmitted()) {
            return "redirect:/artist/uploaded";
        }
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

        if (!agreePersonalInfo || !agreeService || !agreeAge) {
            model.addAttribute("errorMessage", "모든 필수 약관에 동의해야 합니다.");
            return "artist/termsForm";
        }

        Member member = memberService.getCurrentMember();
        member.setAgreedToTerms(true);
        memberService.save(member);

        return "redirect:/artist/upload-proof";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload-proof")
    public String showProofUploadForm(Model model, Principal principal) {
        Member member = memberService.getCurrentMember();

        // 약관 동의 여부 확인
        if (!member.isAgreedToTerms()) {
            return "redirect:/artist/terms";
        }

        model.addAttribute("member", member);
        return "artist/proofUploadForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload-proof")
    public String submitProofUpload(
            @RequestParam(name = "agree_personal_info_artist", required = false, defaultValue = "false") boolean agreePersonalInfoArtist,
            @RequestParam(name = "agree_service_artist", required = false, defaultValue = "false") boolean agreeServiceArtist,
            @RequestParam("proofFile") MultipartFile proofFile,
            Principal principal,
            Model model) {

        Member member = memberService.getCurrentMember();

        // 필수 약관에 동의하지 않은 경우
        if (!agreePersonalInfoArtist || !agreeServiceArtist) {
            model.addAttribute("errorMessage", "모든 필수 약관에 동의해야 합니다.");
            return "artist/proofUploadForm";
        }

        // 파일이 업로드되지 않은 경우
        if (proofFile.isEmpty()) {
            model.addAttribute("errorMessage", "파일을 선택해야 합니다.");
            return "artist/proofUploadForm";
        }

        // 약관 동의 정보 저장
        member.setAgreePersonalInfo(true);
        member.setAgreeService(true);
        memberService.save(member);

        // 약관에 동의하고 파일이 정상적으로 업로드된 경우
        artistService.uploadProofFile(member, proofFile);

        return "redirect:/artist/uploaded";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/uploaded")
    public String showUploadConfirmation(Model model) {
        Member member = memberService.getCurrentMember();

        if (!member.isApprovedArtist()) {
            // 승인이 아직 이루어지지 않은 경우
            model.addAttribute("statusMessage", "승인 요청 중입니다.");
            model.addAttribute("canCreateProfile", false);  // 프로필 생성 버튼 숨김
        } else {
            // 승인이 완료된 경우
            model.addAttribute("statusMessage", "승인 완료되었습니다.");
            model.addAttribute("canCreateProfile", true);  // 프로필 생성 버튼 표시
        }

        return "artist/uploadedConfirmation";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/pending-approval")
    public String viewPendingApprovals(Model model) {
        List<Member> pendingMembers = artistService.getMembersPendingApproval();
        List<Member> approvedMembers = artistService.getMembersApproved();
        model.addAttribute("pendingMembers", pendingMembers);
        model.addAttribute("approvedMembers", approvedMembers);
        return "artist/pendingApprovals";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/approve/{id}")
    public String approveProof(@PathVariable("id") Long memberId) {
        artistService.approveMember(memberId);
        return "redirect:/artist/pending-approval";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/reject/{id}")
    public String rejectProof(@PathVariable("id") Long memberId) {
        artistService.rejectMember(memberId);
        return "redirect:/artist/pending-approval";

    }


    @GetMapping("/list")
    public String showArtistList(Model model, Principal principal) {
        List<Artist> artists = artistService.getAllArtists();
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("artists", artists);
        model.addAttribute("member", currentMember);
        return "artist/artistList";
    }

    @PreAuthorize("hasAuthority('ARTIST')")
    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        Member member = memberService.getCurrentMember();

        if (!member.isApprovedArtist()) {
            return "redirect:/artist/uploaded";
        }
        // 현재 로그인된 사용자가 이미 작가인지 확인
        Optional<Artist> existingArtist = artistService.getArtistByMember(member);

        if (existingArtist.isPresent()) {
            // 이미 작가로 등록되어 있는 경우
            model.addAttribute("errorMessage", "이미 작가로 등록되어 있습니다.");
            return "artist/error"; // 또는 다른 적절한 페이지로 리디렉션
        }
        model.addAttribute("artistForm", new ArtistForm());
        return "artist/artistForm";
    }

    @PreAuthorize("hasAuthority('ARTIST')")
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
                artistForm.getArtistAdds()
        );

        return "redirect:/artist/profile/" + artist.getId();
    }


    @PreAuthorize("hasAuthority('ARTIST')")
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

    @PreAuthorize("hasAuthority('ARTIST')")
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

        return "redirect:/artist/profile/" + id;
    }

    @PreAuthorize("hasAuthority('ARTIST')")
    @GetMapping("/delete/{id}")
    public String artistDelete(Principal principal, @PathVariable("id") Integer id) {
        Artist artist = this.artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.artistService.delete(artist);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/favorite/{id}")
    public ResponseEntity<String> favoriteArtist(@PathVariable("id") Integer artistId) {
        Member member = memberService.getCurrentMember();

        // 즐겨찾기 추가 또는 제거
        boolean isFavorited = artistService.toggleFavoriteArtist(member, artistId);

        // 결과에 따라 상태 메시지 반환
        return ResponseEntity.ok(isFavorited ? "Added" : "Removed");
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favorites")
    public String showFavoriteArtists(Model model) {
        Member member = memberService.getCurrentMember();
        Set<Artist> favoriteArtists = member.getFavoriteArtists();
        model.addAttribute("favoriteArtists", favoriteArtists);
        return "artist/favoriteArtists";
    }
}

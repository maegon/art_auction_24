package com.example.ArtAuction_24.domain.artist.controller;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.entity.ArtistAdd;
import com.example.ArtAuction_24.domain.artist.entity.ContentAdd;
import com.example.ArtAuction_24.domain.artist.entity.TitleAdd;
import com.example.ArtAuction_24.domain.artist.form.ArtistForm;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/artist")
@Controller
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(ArtistForm artistForm) {
        return "artist/artistForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String artistCreate(
            @Valid ArtistForm artistForm,
            BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "artist/artistForm";
        }

        Member member = this.memberService.getCurrentMember();
        Artist artist = this.artistService.create(
                artistForm.getThumbnail(),
                artistForm.getKorName(),
                artistForm.getEngName(),
                artistForm.getBirthDate(),
                artistForm.getTel(),
                artistForm.getMail(),
                artistForm.getMailType(),
                member
        );

        List<ArtistAdd> artistAdds = new ArrayList<>();
        for (String content : artistForm.getArtistAdds()) {
            ArtistAdd artistAdd = new ArtistAdd();
            artistAdd.setContent(content);
            artistAdds.add(artistAdd);
        }

        return "redirect:/artist/profileForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String artistModify(@Valid ArtistForm artistForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "profileForm";
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
                artistForm.getTel(),
                artistForm.getMail(),
                artistForm.getMailType(),
                artistForm.getIntroduce(),
                artistForm.getMajorWork(),
                artistForm.getTitle(),
                artistForm.getContent()
        );

        return "redirect:/artist/profileForm/%s".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String artistModify(ArtistForm artistForm, @PathVariable("id") Long id, Principal principal) {
        Artist artist = artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        artistForm.setKorName(artist.getKorName());
        artistForm.setEngName(artist.getEngName());
        artistForm.setBirthDate(artist.getBirthDate());
        artistForm.setTel(artist.getTel());
        artistForm.setMail(artist.getMail());
        artistForm.setMailType(artist.getMailType());
        artistForm.setIntroduce(artist.getIntroduce());
        artistForm.setMajorWork(artist.getMajorWork());
        artistForm.setTitle(artist.getTitle());
        artistForm.setContent(artist.getContent());

        artistForm.setExistingThumbnailUrl(artist.getThumbnailImg());

        // 추가된 입력란 데이터 처리
        List<ArtistAdd> artistAdds = new ArrayList<>();
        for (String content : artistForm.getArtistAdds()) {
            ArtistAdd artistAdd = new ArtistAdd();
            artistAdd.setContent(content);
            artistAdds.add(artistAdd);
        }

        // 동일하게 contentAdds와 titleAdds 처리
        List<ContentAdd> contentAdds = new ArrayList<>();
        for (String content : artistForm.getContentAdds()) {
            ContentAdd contentAdd = new ContentAdd();
            contentAdd.setContent(content);
            contentAdds.add(contentAdd);
        }

        //
        List<TitleAdd> titleAdds = new ArrayList<>();
        for (String title : artistForm.getTitleAdds()) {
            TitleAdd titleAdd = new TitleAdd();
            titleAdd.setTitle(title);
            titleAdds.add(titleAdd);
        }

        return "artist/profileForm";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String artistDelete(Principal principal, @PathVariable("id") Long id) {
        Artist artist = this.artistService.getArtist(id);

        if (!artist.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        artistService.delete(artist);

        return "redirect:/";
    }
}

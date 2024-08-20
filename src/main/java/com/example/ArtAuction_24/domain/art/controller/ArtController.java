package com.example.ArtAuction_24.domain.art.controller;

import com.example.ArtAuction_24.domain.art.entity.Art;
import com.example.ArtAuction_24.domain.art.form.ArtForm;
import com.example.ArtAuction_24.domain.art.service.ArtService;
import com.example.ArtAuction_24.domain.artist.entity.*;
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
import java.util.List;

@RequestMapping("/art")
@Controller
@RequiredArgsConstructor
public class ArtController {
    private final ArtService artService;
    private final ArtistService artistService;
    private final MemberService memberService;

    @GetMapping("/list/{id}")
    public String list(Model model, @PathVariable("id") Integer id, Principal principal) {
        Member currentMember = memberService.getCurrentMember();
        List<Art> list = artService.getList();

        // 만약 리스트가 null이거나 비어있다면 빈 리스트로 초기화
        if (list == null || list.isEmpty()) {
            list = List.of();
        }

        // 모델에 추가하여 뷰로 전달
        model.addAttribute("list", list);
        model.addAttribute("currentMember", currentMember);

        return "art/artList";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        // 단일 Art 객체를 가져와서 모델에 추가
        Art a = this.artService.getArt(id);
        model.addAttribute("art", a);

        // Art 객체의 리스트를 가져와서 모델에 추가
        List<Art> list = this.artService.getList();  // 모든 Art 객체의 리스트를 가져온다고 가정
        model.addAttribute("list", list);

        return "art/artDetail";  // artDetail.html로 리턴
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("artForm", new ArtForm());
        return "art/artForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(
            @ModelAttribute @Valid ArtForm artForm,
            BindingResult bindingResult,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            return "art/artForm";
        }

        if (thumbnail.isEmpty()) {
            System.out.println("파일이 업로드되지 않았습니다.");
            return "art/artForm";
        } else {
            System.out.println("파일 업로드 성공: " + thumbnail.getOriginalFilename());
        }

        // 현재 사용자(Member) 정보 가져오기
        Member member = this.memberService.getCurrentMember();
        // 현재 사용자의 아티스트 정보 가져오기
        Artist artist = this.artistService.findByMember(member);
        // Art 객체 생성 및 저장
        Art art = artService.create(
                thumbnail,
                artForm.getKorTitle(),
                artForm.getEngTitle(),
                artForm.getWidth(),
                artForm.getHeight(),
                artForm.getUnit(),
                artForm.getTechnique(),
                artForm.getPrice(),
                artForm.getPlace(),
                artForm.getArtIntroduction(),
                member,
                artist
        );

        if (artist == null) {
            System.out.println("아티스트 정보를 찾을 수 없습니다.");
            return "error/artistNotFound";
        }
        System.out.println("thumbnail" + thumbnail);
        System.out.println("getKorTitle" + artForm.getKorTitle());
        System.out.println("getEngTitle" + artForm.getEngTitle());
        System.out.println("getWidth" + artForm.getWidth());
        System.out.println("getHeight" + artForm.getHeight());
        System.out.println("getUnit" + artForm.getUnit());
        System.out.println("getHeight" + artForm.getHeight());
        System.out.println("getUnit" + artForm.getUnit());



        return "redirect:/art/list/" + artist.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String artModify( @Valid ArtForm artForm,
                             @PathVariable("id") Integer id,
                             BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {

            return "art/artForm";
        }

        Art art = artService.getArt(id);

        if (!art.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        artService.modify(
                art,
                artForm.getThumbnail(),
                artForm.getKorTitle(),
                artForm.getEngTitle(),
                artForm.getWidth(),
                artForm.getHeight(),
                artForm.getUnit(),
                artForm.getTechnique(),
                artForm.getPrice(),
                artForm.getPlace(),
                artForm.getArtIntroduction()
        );

        return "redirect:/art/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String artModify(ArtForm artForm, @PathVariable("id") Integer id, Principal principal) {
        Art art = artService.getArt(id);

        if (!art.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        artForm.setKorTitle(art.getKorTitle());
        artForm.setEngTitle(art.getEngTitle());
        artForm.setWidth(artForm.getWidth());
        artForm.setHeight(art.getHeight());
        artForm.setUnit(art.getUnit());
        artForm.setTechnique(art.getTechnique());
        artForm.setPrice(art.getPrice());
        artForm.setPlace(art.getPlace());
        artForm.setArtIntroduction(art.getArtIntroduction());


        return "art/artForm";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String artDelete(Principal principal, @PathVariable("id") Integer id) {
        Art art = artService.getArt(id);

        if (!art.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        // 그 후 작가를 삭제
        this.artService.delete(art);

        artService.delete(art);

        return "redirect:/";
    }

}
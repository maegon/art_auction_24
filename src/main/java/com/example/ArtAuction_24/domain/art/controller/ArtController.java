package com.example.ArtAuction_24.domain.art.controller;

import com.example.ArtAuction_24.domain.art.entity.Art;
import com.example.ArtAuction_24.domain.art.form.ArtForm;
import com.example.ArtAuction_24.domain.art.service.ArtService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@RequestMapping("/art")
@Controller
@RequiredArgsConstructor
public class ArtController {
    private final ArtService artService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Art> paging = this.artService.getList(page, kw);
        model.addAttribute("paging", paging);

        return "art/artList";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Art a = this.artService.getArt(id);
        model.addAttribute("art", a);

        return "art/artDetail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(ArtForm artForm) {
        return "art/artForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String artCreate(
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("korTitle") String korTitle,
            @RequestParam("engTitle") String engTitle,
            @RequestParam("artist") String artist,
            @RequestParam("width") String width,
            @RequestParam("height") String height,
            @RequestParam("unit") String unit,
            @RequestParam("technique") String technique,
            @RequestParam("price") String price,
            @RequestParam("place") String place,
            @RequestParam("artIntroduction") String artIntroduction,
            Principal principal) {
        if (thumbnail.isEmpty() || korTitle.isEmpty() || engTitle.isEmpty() || artist.isEmpty() || width.isEmpty() || height.isEmpty() || unit.isEmpty() || technique.isEmpty() || price.isEmpty() || place.isEmpty() || artIntroduction.isEmpty()) {
            return "art/artForm";
        }

        Member member = memberService.getCurrentMember();
        Art art = artService.create(thumbnail,korTitle,engTitle,artist,width,height,unit,technique,price,place,artIntroduction,member);

        return "redirect:/art/artList";
    }
}

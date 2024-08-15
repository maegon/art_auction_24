package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.dto.DailyVisitorData;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdmHomeController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final ArtistService artistService;
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String index() {
        return "admin/home/admMain";
    }

    @GetMapping("/home/admMain")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMain(Model model) {
        return "admin/home/admMain";
    }

    @GetMapping("/product/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showProduct() {
        return "admin/product/list";
    }

    @GetMapping("/member/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMember(
            Model model
    ) {
        List<Member> memberList = memberService.getMemberList();
        model.addAttribute("memberList", memberList);
        return "admin/member/list";
    }

}
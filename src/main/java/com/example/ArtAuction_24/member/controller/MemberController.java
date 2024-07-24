package com.example.ArtAuction_24.member.controller;

import com.example.ArtAuction_24.member.form.MemberForm;
import com.example.ArtAuction_24.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/join")
    public String showJoin () {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberForm memberForm) {
        memberService.join(memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail(), memberForm.getNickname(), memberForm.getPhoneNumber(), memberForm.getAddress(), memberForm.getImage());

        return "redirect:/member/login";
    }

}

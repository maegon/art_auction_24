package com.example.ArtAuction_24.member.controller;

import com.example.ArtAuction_24.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "redirect:/main";
    }

    @GetMapping("/join")
    public String showJoin () {
        return "redirect:/main";
    }

}

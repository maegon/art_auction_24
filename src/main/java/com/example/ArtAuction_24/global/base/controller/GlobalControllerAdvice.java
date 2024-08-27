package com.example.ArtAuction_24.global.base.controller;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final MemberService memberService;

    public GlobalControllerAdvice(MemberService memberService) {
        this.memberService = memberService;
    }

    @ModelAttribute("member")
    public Member populateMember(Principal principal, Model model) {
        if (principal != null) {
            return memberService.getMember(principal.getName());
        }
        return null; // Or return a default object or handle anonymous users
    }
}
package com.example.ArtAuction_24.recharge.controller;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.recharge.entity.Recharge;
import com.example.ArtAuction_24.recharge.repository.RechargeRepository;
import com.example.ArtAuction_24.recharge.service.RechargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recharge")
public class RechargeViewController {
    private final RechargeRepository rechargeRepository;
    private final RechargeService rechargeService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recharge")
    public String showRechargePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 현재 로그인한 사용자의 username 가져오기

        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Recharge> rechargeList = rechargeService.findByMember(member);

        model.addAttribute("customerName", member.getNickname());
        model.addAttribute("email", member.getEmail());
        model.addAttribute("phoneNumber", member.getPhoneNumber());
        model.addAttribute("totalBalance", member.getFormattedbalance());
        model.addAttribute("rechargeList", rechargeList);

        return "recharge/recharge";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/success")
    public String showRechargeSuccessPage(@RequestParam("amount") Long amount, Model model) {
        // 현재 로그인한 사용자의 username 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        if (amount <= 0 || amount > 2000000) {
            return "recharge/fail";
        }

        // 충전 금액을 사용자 잔액에 추가
        memberService.updateBalance(username, amount);

        return "recharge/success";
    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/fail")
    public String showRechargeFailPage(Model model) {
        // 실패 페이지를 위한 추가 처리 (예: 에러 메시지 처리)

        return "recharge/fail";
    }

}

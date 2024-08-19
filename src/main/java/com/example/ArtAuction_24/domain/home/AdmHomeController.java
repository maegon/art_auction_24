package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdmHomeController {
    private final MemberService memberService;
    private final ProductService productService;
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
    public String showProduct(
            Model model
    ) {
        List<Product> productList = productService.getProductList();
        model.addAttribute("productList", productList);
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

    // 작가 신청
//    @GetMapping("/member/artistApplicant")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public String showArtistApplicant(
//            Model model
//    ) {
//        List<Member> artistApplicantList = memberService.getArtistApplicantList();
//        model.addAttribute("artistApplicantList", artistApplicantList);
//        return "admin/member/artistApplicant";
//    }

    @PutMapping("/member/{memberId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateMemberVerAdmin(
            @PathVariable("memberId") Long memberId,
            @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Member> memberOptional = memberService.findById(memberId);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

                if (updates.containsKey("role")) {
                    member.setRole(MemberRole.valueOf((String) updates.get("role")));
                }

                if (updates.containsKey("isActive")) {
                    member.setIsActive((Boolean) updates.get("isActive"));
                }

                memberService.save(member);

                response.put("success", true);
                response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "회원 정보를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
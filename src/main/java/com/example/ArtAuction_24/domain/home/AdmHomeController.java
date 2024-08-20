package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.dto.AdmUpdateMemberRequest;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.post.form.PostForm;
import com.example.ArtAuction_24.domain.post.service.PostService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdmHomeController {
    private final MemberService memberService;
    private final ProductService productService;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final ArtistService artistService;
    private final PostService postService;
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
    public String showMember(Model model) {
        List<Member> memberList = memberService.getMemberList();
        model.addAttribute("memberList", memberList);
        return "admin/member/list";
    }

    @PutMapping("/member/{memberId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateMemberVerAdmin(
            @PathVariable("memberId") Long memberId,
            @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();

        return memberService.findById(memberId).map(member -> {
            if (updates.containsKey("role") && updates.get("role") instanceof String) {
                member.setRole(MemberRole.valueOf((String) updates.get("role")));
            }

            if (updates.containsKey("isActive") && updates.get("isActive") instanceof Boolean) {
                member.setIsActive((Boolean) updates.get("isActive"));
            }

            memberService.save(member);
            response.put("success", true);
            response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "회원 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
    }

    @GetMapping("/question/manage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showQuestionManage(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("answered"), Sort.Order.desc("id")));
        Page<Question> questionPage = questionService.findAll(pageable);
        model.addAttribute("questionPage", questionPage);
        return "admin/question/manage";
    }




    @GetMapping("/question/faqManage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showFaqList(Model model) {
        return "admin/question/faqManage";
    }

    @GetMapping("/question/faqWrite")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showQuestionWrite(Model model) {
        return "admin/question/faqWrite";
    }

    @PostMapping("/question/faqWrite")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String QuestionWrite(@Valid PostForm postForm, BindingResult bindingResult, Principal principal) {
        Member member = memberService.getMember(principal.getName());

        if (bindingResult.hasErrors()) {
            return "admin/question/faqWrite";
        }
        postService.create(postForm, member);


        return "admin/question/faqManage";
    }




}
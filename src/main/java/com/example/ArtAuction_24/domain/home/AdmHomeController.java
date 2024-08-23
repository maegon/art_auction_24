package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.member.dto.ApiResponse;
import com.example.ArtAuction_24.domain.member.dto.MemberUpdateRequest;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.post.entity.Post;
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
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdmHomeController {
    private final MemberService memberService;
    private final ProductService productService;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final AuctionService auctionService;
    private final ArtistService artistService;
    private final PostService postService;
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

    @GetMapping("/auction/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAuction(Model model) {
        List<Auction> auctionList = auctionService.getAuctionLit();
        model.addAttribute("auctionList", auctionList);
        return "admin/auction/list";
    }

    // 회원 권한 설정 저장
    @PutMapping("/member/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable("memberId") long memberId, @RequestBody MemberUpdateRequest request) {
        try {
            memberService.updateMemberStatusAndRole(memberId, request);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

//    @PutMapping("/member/{memberId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Map<String, Object>> updateMemberVerAdmin(
//            @PathVariable("memberId") Long memberId,
//            @RequestBody Map<String, Object> updates) {
//        Map<String, Object> response = new HashMap<>();
//
//        // 회원 ID로 회원 조회
//        return memberService.findById(memberId).map(member -> {
//            // 권한 업데이트
//            if (updates.containsKey("role") && updates.get("role") instanceof String) {
//                try {
//                    member.setRole(MemberRole.valueOf((String) updates.get("role")));
//                } catch (IllegalArgumentException e) {
//                    response.put("success", false);
//                    response.put("message", "잘못된 역할 값입니다.");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//                }
//            }
//
//            // 활성화 상태 업데이트
//            if (updates.containsKey("isActive") && updates.get("isActive") instanceof Boolean) {
//                member.setIsActive((Boolean) updates.get("isActive"));
//            }
//
//            // 변경 사항 저장
//            memberService.save(member);
//
//            response.put("success", true);
//            response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
//            return ResponseEntity.ok(response);  // 200 OK 상태 반환
//        }).orElseGet(() -> {
//            response.put("success", false);
//            response.put("message", "회원 정보를 찾을 수 없습니다.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 404 상태 반환
//        });
//    }



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
        List<Post> postList = postService.findAll();
        model.addAttribute("postList",postList);
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


        return "redirect:/admin/question/faqManage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/faqManage/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        if (!post.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postService.delete(post);
        return "redirect:/";
    }




}
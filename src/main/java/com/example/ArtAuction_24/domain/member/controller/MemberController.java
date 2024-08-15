package com.example.ArtAuction_24.domain.member.controller;


import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.form.MemberAddressForm;
import com.example.ArtAuction_24.domain.member.form.MemberForm;
import com.example.ArtAuction_24.domain.member.form.MemberModifyForm;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final ProductService productService;
    private final ArtistService artistService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = memberService.checkUsernameExists(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberService.checkNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(
            @ModelAttribute @Valid MemberForm memberForm,
            BindingResult bindingResult) {



        // 이메일 및 주소 필드 결합
        String email = memberForm.getLogemailT() + "@" + memberForm.getLogemail();
        memberForm.setEmail(email);

        String address = memberForm.getAddress1() + ", " + memberForm.getAddress2() + memberForm.getAddress3();
        memberForm.setAddress(address);


        System.out.println("============== test 2 ==============");
        System.out.println(email);
        System.out.println(address);

        System.out.println(memberForm.getUsername());
        System.out.println(memberForm.getNickname());
        System.out.println(memberForm.getPassword());
        System.out.println(memberForm.getPhoneNumber());
        System.out.println(memberForm.getProviderTypeCode());
        System.out.println(memberForm.getAddress());
        System.out.println(memberForm.getEmail());
        System.out.println("============== test 2 ==============");


        // BindingResult 에러 출력
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            return "member/login";
        }



        // 회원가입 서비스 호출
        try {

            Member member = this.memberService.join(
                    memberForm.getProviderTypeCode(),
                    memberForm.getUsername(),
                    memberForm.getPassword(),
                    memberForm.getEmail(),
                    memberForm.getNickname(),
                    memberForm.getPhoneNumber(),
                    memberForm.getAddress()
            );

            if (member == null) {
                System.out.println("DB에 회원 정보가 저장되지 않았습니다.");
                return "member/login";
            }

            // 회원가입 성공 시 이메일 발송
            String bodyText = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Art Auction에 가입해주신 것을 진심으로 환영합니다! 저희는 미술 작품을 온라인 경매를 통해 작품을 공유하고 소장할 수 있는 공간을 만들고자 합니다.<br><br>" +
                            "Art Auction에서는 작가가 손수 작업한 예술품이 많은 이들과 함께 소중한 경험, 건강한 힐링이 될 것입니다.<br><br>" +
                            "저희는 Art Auction을 이용해주시는 고객님들께 아래 정보를 제공합니다.<br><br>" +
                            "* 다양한 미술 작품과 작가들의 최신 경매 소식을 먼저 접하실 수 있습니다.<br><br>" +
                            "* 원하는 경매에 대한 알림과 진행중이거나 예정된 경매를 확인하실 수 있습니다.<br><br>" +
                            "* 고객님의 만족을 위한 안전한 경매, 확실한 작품 이전을 하기 위해 노력합니다.<br><br>" +
                            "* 또한, 투명한 경매 과정과 전문적인 지원을 통해 안심하고 거래하실 수 있도록 도와드립니다.<br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 [내 프로필] -> [1:1 문의] 를 통해 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "감사합니다, Art Auction 드림.",
                    memberForm.getUsername()
            );

            emailService.send(memberForm.getEmail(), "Art Auction 가입을 환영합니다!", bodyText);

        } catch (Exception e) {
            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            return "member/login";
        }

        return "redirect:/member/login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("member",member);
        String username = member.getUsername();

        MemberModifyForm memberModifyForm = new MemberModifyForm();
        model.addAttribute("memberModifyForm", memberModifyForm);
        memberModifyForm.setUsername(username); // 로그인된 사용자 아이디 못바꾸게할려고 폼에집어넣음

        Set<Long> likedProductIds = productService.getLikedProductIdsByMember(member.getId());
        model.addAttribute("likedProductIds", likedProductIds);


        List<LikeProduct> likeProductList = productService.getLikeProduct(member);
        model.addAttribute("likeProductList", likeProductList);

        List<Question> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String updateMember(@Valid MemberModifyForm memberModifyForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {


        memberService.updateMember(memberModifyForm, principal.getName());
        return "redirect:/member/logout"; // 업데이트하고나서 로그아웃

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addressUpdate")
    public String updateMemberAddress(@Valid MemberAddressForm memberAddressForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {

        String address = memberAddressForm.getAddress1() + ", " + memberAddressForm.getAddress2() + memberAddressForm.getAddress3();
        memberAddressForm.setAddress(address);


        memberService.updateMemberAddress(memberAddressForm, principal.getName());
        return "redirect:/member/logout"; // 업데이트하고나서 로그아웃

    }




    @GetMapping("/applicantArtist1")
    public String applicantArtist1() {

        return "member/applicantArtist1";
    }
}

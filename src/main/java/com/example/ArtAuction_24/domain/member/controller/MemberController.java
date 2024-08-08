package com.example.ArtAuction_24.domain.member.controller;


import com.example.ArtAuction_24.domain.member.form.MemberForm;
import com.example.ArtAuction_24.domain.member.form.MemberForm2;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final QuestionService questionService;

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

    @PostMapping("/join")
    public String join(@Valid MemberForm memberForm, BindingResult bindingResult, @RequestParam("profileImage") MultipartFile profileImage ) {

        // 이메일 필드를 결합
        String email = memberForm.getLogemailT() + "@" + memberForm.getLogemail();
        memberForm.setEmail(email);

        // 주소 필드를 결합
        String address = memberForm.getAddress1() +", "+ memberForm.getAddress2() + memberForm.getAddress3();
        memberForm.setAddress(address);

        // 프로필 이미지 파일 저장
        String imageFileName = memberService.memberProfileImage(memberForm.getProfileImage());

        System.out.println("username : " + memberForm.getUsername());
        System.out.println("password : " + memberForm.getPassword());
        System.out.println("email : " + memberForm.getEmail());
        System.out.println("nickname : " + memberForm.getNickname());
        System.out.println("phoneNumber : " + memberForm.getPhoneNumber());
        System.out.println("address : " + memberForm.getAddress());
        System.out.println("profileImage : " + memberForm.getProfileImage());


        if (bindingResult.hasErrors()) {
            System.out.println("test error : "+ bindingResult.hasErrors());
            return "member/login"; // 유효성 검사에 오류가 있는 경우
        }

        System.out.println("test22");

        try {

            System.out.println("test33");
            // 회원가입 서비스 호출
            memberService.join(
                    memberForm.getProviderTypeCode(),
                    memberForm.getUsername(),
                    memberForm.getPassword(),
                    memberForm.getEmail(),
                    memberForm.getNickname(),
                    memberForm.getPhoneNumber(),
                    memberForm.getAddress(),
                    imageFileName
            );

//            // 회원가입 성공 시 이메일 발송
//            String bodyText = String.format(
//                    "안녕하세요, <b>%s</b>님<br><br>" +
//                            "Art Auction에 가입해주신 것을 진심으로 환영합니다! 저희는 미술 작품을 온라인 경매를 통해 작품을 공유하고 소장할 수 있는 공간을 만들고자 합니다.<br><br>" +
//                            "Art Auction에서는 작가가 손수 작업한 예술품이 많은 이들과 함께 소중한 경험, 건강한 힐링이 될 것입니다.<br><br>" +
//                            "저희는 Art Auction을 이용해주시는 고객님들께 아래 정보를 제공합니다.<br><br>" +
//                            "* 다양한 미술 작품과 작가들의 최신 경매 소식을 먼저 접하실 수 있습니다.<br><br>" +
//                            "* 원하는 경매에 대한 알림과 진행중이거나 예정된 경매을 확인하실 수 있습니다.<br><br>" +
//                            "* 고객님의 만족을 위한 안전한 경매, 확실한 작품 이전을 하기 위해 노력합니다.<br><br>" +
//                            "* 또한, 투명한 경매 과정과 전문적인 지원을 통해 안심하고 거래하실 수 있도록 도와드립니다.<br><br>" +
//                            "더 궁금하신 점이나 도움이 필요하신 경우 [내 프로필] -> [1:1 문의] 를 통해 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
//                            "감사합니다, Art Auction 드림.",
//                    memberForm.getUsername()
//            );

//            emailService.send(memberForm.getEmail(), "Art Auction 가입을 환영합니다!", bodyText);

        } catch (Exception e) {
            return "member/login";
        }

        return "redirect:/member/login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("member",member);

        MemberForm2 memberForm2 = new MemberForm2();
        model.addAttribute("memberForm2", memberForm2);


        List<Question> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String updateMember(@Valid MemberForm2 memberForm2, BindingResult result, Principal principal, Model model) {


        memberService.updateMember(memberForm2, principal.getName());
        return "redirect:/member/myPage";

    }

}

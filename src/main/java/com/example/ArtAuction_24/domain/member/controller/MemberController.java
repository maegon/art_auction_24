package com.example.ArtAuction_24.domain.member.controller;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.form.MemberForm;
import com.example.ArtAuction_24.domain.member.form.MemberForm2;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final QuestionService questionService;
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @PostMapping("/join")
    public String join(@Valid MemberForm memberForm, BindingResult bindingResult, Model model) {
        //, @RequestParam("profileImage") MultipartFile profileImage 나중에 프로필 사진 미리보기 추가 하게 되면..
        // 작가로 선택된 회원의 예술활동증명서도 추가해봐야 함
        if (bindingResult.hasErrors()) {
            return "redirect:/member/login";
        }
        try {
            String imageFileName = myProfileImage(memberForm.getProfileImage());
            memberService.join(memberForm.getProviderTypeCode(), memberForm.getUsername(), memberForm.getPassword(), memberForm.getEmail(), memberForm.getNickname(), memberForm.getPhoneNumber(), memberForm.getAddress(), imageFileName);
            String bodyText = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Art Auction을 가입해주신 것을 진심으로 환영합니다! 저희는 미술 작품을 온라인 경매를 통해 작품을 공유하고 소장할 수 있는 공간을 만들고자 합니다.<br><br>" +
                            "Art Auction에서는 작가가 손수 작업한 예술품이 많은 이들과 함께 소중한 경험, 건강한 힐링이 될 것입니다.<br><br>" +
                            "저희는 Art Auction을 이용해주시는 고객님들께 아래 정보를 제공합니다.<br><br>" +
                            "* 다양한 미술 작품과 작가들의 최신 경매 소식을 먼저 접하실 수 있습니다.<br><br>" +
                            "* 원하는 경매에 대한 알림과 진행중이거나 예정된 경매을 확인하실 수 있습니다.<br><br>" +
                            "* 고객님의 만족을 위한 안전한 경매, 확실한 작품 이전을 하기 위해 노력합니다.<br><br>" +
                            "* 또한, 투명한 경매 과정과 전문적인 지원을 통해 안심하고 거래하실 수 있도록 도와드립니다.<br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 [내 프로필] -> [1:1 문의] 를 통해 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "감사합니다, Art Auction 드림.",
                    memberForm.getUsername()
            );


            emailService.send(memberForm.getEmail(), "Art Auction 가입을 환영합니다!", bodyText);

        } catch (IllegalStateException e) {
            model.addAttribute("joinError", "이미 중복된 이메일 또는 아이디입니다");
            return "redirect:/member/login";
        }


        return "redirect:/member/login";
    }

    public String myProfileImage(MultipartFile profileImage) {
        // 이미지 저장 디렉토리 경로

        String uploadDir = genFileDirPath + "\\image\\profileImageUpload";

        // 디렉토리가 존재하지 않으면 생성합니다.
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }
        // 파일명 중복을 피하기 위해 임의의 파일명을 생성합니다.
        String fileName = UUID.randomUUID().toString(); // UUID로 파일명 생성
        String imageFileName = fileName + ".png"; // 파일 확장자 지정
        // 파일을 저장합니다.
        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }
        // 저장된 파일의 상대 경로를 반환합니다.
        return "/images/profileImageUpload/" + imageFileName;
    }


    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {

        MemberForm2 memberForm2 = new MemberForm2();
        model.addAttribute("memberForm2", memberForm2);

        List<Question> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "member/myPage";
    }

    @PostMapping("/update")
    public String updateMember(@Valid MemberForm2 memberForm2, BindingResult result, Principal principal, Model model) {


        memberService.updateMember(memberForm2, principal.getName());
        return "redirect:/member/myPage";

    }

}

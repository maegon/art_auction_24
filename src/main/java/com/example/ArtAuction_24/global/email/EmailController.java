package com.example.ArtAuction_24.global.email;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/sendmail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/find-password")
    public String sendPasswordMail(@RequestParam("username") String username, @RequestParam("email") String email,
                                                   RedirectAttributes redirectAttributes) {
        // 입력된 아이디와 이메일로 사용자 찾기
        Optional<Member> optionalMember = memberRepository.findByUsernameAndEmail(username, email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 임시 비밀번호 생성 및 암호화
            String tempPassword = emailService.createTemPw();
            member.setPassword(passwordEncoder.encode(tempPassword));
            memberRepository.save(member);

            // 이메일 본문 작성
            String body = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Art Auction 임시 비밀번호 발급 안내입니다.<br><br>" +
                            "회원님의 임시 비밀번호는 <b>%s</b> 입니다.<br><br>" +
                            "로그인 후에 비밀번호를 변경해주세요!" +
                            "<br><br>" +
                            "Art Auction 드림.",
                    member.getUsername(), tempPassword
            );

            // 이메일 발송
            emailService.send(email, "[Art Auction] 임시 비밀번호 발급 안내", body);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("message", "임시 비밀번호가 이메일로 발송되었습니다.");
            return "redirect:/member/login";
        } else {
            // 아이디와 이메일이 일치하지 않을 때 예외 처리
            redirectAttributes.addFlashAttribute("error", "해당 아이디와 이메일을 가진 회원을 찾을 수 없습니다.");
            return "redirect:/member/login";
        }
    }

    @PostMapping("/confirmCode")
    public ResponseEntity<Map<String, String>> sendConfirmationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = emailService.createConfirmCode();

        // 이메일로 인증 코드 전송 (구현 필요)
        emailService.sendConfirmCode(email, code);

        // 클라이언트로 인증 코드 전달
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        return ResponseEntity.ok(response);
    }

}

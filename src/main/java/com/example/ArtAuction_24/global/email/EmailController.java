package com.example.ArtAuction_24.global.email;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/sendmail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/password")
    public ResponseEntity<Void> sendPasswordMail(@RequestBody EmailRequestDto emailRequestDto) {
        // 이메일 수신자
        String email = emailRequestDto.getEmail();
        // 임시 비밀번호 생성
        String code = emailService.createTemPw();

        Optional<Member> optionalMember = memberRepository.findByUsernameAndEmail(emailRequestDto.getUsername(), emailRequestDto.getEmail());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 새로운 비밀번호를 암호화하여 저장
            member.setPassword(passwordEncoder.encode(code));
            memberRepository.save(member);

            // 이메일 본문 내용
            String body = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Art Auction 임시 비밀번호 발급 안내입니다.<br><br>" +
                            "회원님의 임시 비밀번호는  %s  입니다.<br><br>" +
                            "로그인 후에 비밀번호를 변경해주세요!" +
                            "<br><br>" +
                            "Art Auction 드림.",
                    member.getUsername(), code
            );
            // 이메일 발송
            emailService.send(emailRequestDto.getEmail(), "[Art Auction] 임시비밀번호 발급 안내입니다.", body);

            // 클라이언트로 반환
            return ResponseEntity.ok().build();
        } else {
            throw new RuntimeException("해당 아이디와 이메일을 가진 회원을 찾을 수 없습니다.");
        }
    }

    @GetMapping("/password")
    public ModelAndView getPasswordPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sendmail/password");
        return modelAndView;
    }

    @PostMapping("/confirmCode")
    public ResponseEntity<EmailResponseDto> sendCodeMail(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            // 이메일 수신자
            String email = emailRequestDto.getEmail();

            // 인증번호 생성
            String code = emailService.createConfirmCode();

            // 이메일 본문 내용
            String body = String.format(
                    "안녕하세요, <b>Art Auction 입니다.<br><br>" +
                            "Art Auction 이메일 인증번호 발급 안내입니다.<br><br>" +
                            "회원님의 인증 번호는 %s 입니다.<br><br>" +
                            "인증번호 입력란에 작성해주세요." +
                            "<br><br>" +
                            "Art Auction 드림.",
                    code
            );

            // 이메일 발송
            emailService.send(email, "[Art Auction] 이메일 인증번호 발급 안내입니다.", body);

            // 인증번호를 클라이언트로 반환
            EmailResponseDto responseDto = new EmailResponseDto(code);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 로그 기록
            e.printStackTrace();

            // 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

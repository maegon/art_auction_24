package com.example.ArtAuction_24.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(body, true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage); // 메일 발송

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 비밀번호 잊어버릴 시 임시 비밀번호 발급
    public String createTemPw() {
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }

        return codeBuilder.toString();
    }

    // 회원가입 시 이메일 인증을 위한 인증 번호 발급
    public String createConfirmCode() {
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        String characters = "0123456789";
        int length = 4;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }

        return codeBuilder.toString();
    }

    public void sendConfirmCode(String to, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String subject = "[Art Auction] 이메일 인증 번호 발급 안내입니다.";
        String body = String.format(
                "안녕하세요, <b>Art Auction 입니다.<br><br>" +
                        "Art Auction 이메일 인증번호 발급 안내입니다.<br><br>" +
                        "회원님의 인증 번호는 %s 입니다.<br><br>" +
                        "인증번호 입력란에 작성해주세요." +
                        "<br><br>" +
                        "Art Auction 드림.",
                code
        );
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(body, true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage); // 메일 발송

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

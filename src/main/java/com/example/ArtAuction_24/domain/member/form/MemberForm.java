package com.example.ArtAuction_24.domain.member.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
public class MemberForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    private MultipartFile profileImage;

    private String providerTypeCode;

    private String logemailT;
    private String logemail;

    private String address1;
    private String address2;
    private String address3;
}

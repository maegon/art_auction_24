package com.example.ArtAuction_24.domain.member.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private String address;

    private String providerTypeCode;

    private String logemailT;
    private String logemail;

    private String address1;
    private String address2;
    private String address3;
}

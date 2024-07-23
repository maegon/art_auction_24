package com.example.ArtAuction_24.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.io.File;

@Data
public class MemberForm {
    @Size(min = 4, max = 24)
    @NotBlank
    private String username;

    @Size(min = 6, max = 24)
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#])[a-z\\d!@#]{6,24}$",
            message = "각각 하나 이상의 소문자, 숫자, 특수문자(!,@,#)를 포함하여 최소 6자리 ~ 최대 24자리 까지 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$",
            message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?!.*(admin|관리자|어드민)).*$",
            message = "부적절한 단어가 포함되어 있습니다.")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "000-0000-0000 형식에 맞게 작성해주세요.")
    private String phoneNumber;

    @NotBlank
    private String address;

    private File image;

}

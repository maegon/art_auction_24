package com.example.ArtAuction_24.domain.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
public class MemberForm2 {

    @NotBlank
    private String username;

    @Size(min = 6, max = 24)
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String email;

    @NotBlank
    private String nickname;
}

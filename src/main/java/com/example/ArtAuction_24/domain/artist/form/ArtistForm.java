package com.example.ArtAuction_24.domain.artist.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.groovy.util.FastArray;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ArtistForm {

    @NotBlank(message = "자기소개는 필수입니다.")
    @Size(max = 2000, message = "자기소개를 입력하세요")
    private String introduce;

    @NotBlank(message = "대표작품은 필수입니다.")
    @Size(max = 200, message = "대표작품을 입력하세요 ex) <나비>(2012): 각 157X130, 수채화")
    private String majorWork;

    @Size(max = 200, message = "제목을 입력하세요.")
    private String title;

    @Size(max = 2000, message = "내용을 입력하세요.")
    private String content;

    private MultipartFile thumbnail;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 30, message = "이름을 입력하세요. ex) 차은우")
    private String korName;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 50, message = "이름을 입력하세요. ex) Eunwoo Cha")
    private String engName;

    @NotBlank(message = "생년월일은 필수입니다.")
    @Size(max = 8, message = "생년월일 8자리를 입력하세요")
    private String birthDate;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(max = 11, message = "전화번호 11자리를 입력하세요")
    private String tel;

    @NotBlank(message = "이메일은 필수입니다.")
    @Size(max = 30, message = "이메일을 입력해주세요.")
    private String mail;

    private String mailType;

    private String existingThumbnailUrl;

    private List<String> artistAdds = new ArrayList<>();
    private List<String> contentAdds = new ArrayList<>();
    private List<String> titleAdds = new ArrayList<>();
}


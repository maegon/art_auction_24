package com.example.ArtAuction_24.domain.artist.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ArtistForm {

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

    @Size(max = 500, message = "자기소개 입력해주세요.")
    private String introduce;

    private String existingThumbnailUrl;

    // Getters and Setters
    @Setter
    @Getter
    private List<String> artistAdds;
    @Setter
    @Getter
    private List<String> titleAdds;
    @Setter
    @Getter
    private List<String> contentAdds;
    @Setter
    @Getter
    private List<String> titleContentAdds;
    @Setter
    @Getter
    private List<String> yearContentAdds;
    @Setter
    @Getter
    private List<String> widthContentAdds;
    @Setter
    @Getter
    private List<String> heightContentAdds;
    @Setter
    @Getter
    private List<String> unitContentAdds;
    @Setter
    @Getter
    private List<String> techniqueContentAdds;

}
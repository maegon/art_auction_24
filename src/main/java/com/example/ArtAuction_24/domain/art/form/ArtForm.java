package com.example.ArtAuction_24.domain.art.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ArtForm {
    private MultipartFile thumbnail;

    private String existingThumbnailUrl;

    @NotBlank(message = "작품명은 필수입니다.")
    @Size(max = 200)
    private String korTitle;
    @NotBlank(message = "작품명은 필수입니다.")
    @Size(max = 200)
    private String engTitle;
    @NotBlank(message = "길이는 필수입니다.")
    @Size(max = 200)
    private String width;
    @NotBlank(message = "높이는 필수입니다.")
    @Size(max = 200)
    private String height;
    @NotBlank(message = "단위 필수입니다.")
    @Size(max = 200)
    private String unit;
    @NotBlank(message = "작품기법은 필수입니다.")
    @Size(max = 200)
    private String technique;
    @NotBlank(message = "작품소개는 필수입니다.")
    @Size(max = 2000)
    private String artIntroduction;
    @Size(max = 200)
    private String price;
    @Size(max = 200)
    private String place;
}
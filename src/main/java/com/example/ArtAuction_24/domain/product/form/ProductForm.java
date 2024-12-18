package com.example.ArtAuction_24.domain.product.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductForm {

    @NotBlank(message = "작품명은 필수입니다.")
    @Size(max = 50)
    private String title;

    @NotBlank(message = "작품 설명은 필수입니다.")
    @Size(max = 500)
    private String description;

    @NotBlank(message = "사용된 재료는 필수입니다.")
    @Size(max = 20)
    private String medium;

    @NotNull(message = "가로 크기는 필수입니다.")
    private Long width;

    @NotNull(message = "세로 크기는 필수입니다.")
    private Long height;

    @NotNull(message = "시작가는 필수 입력 사항입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "시작가는 0보다 커야 합니다.")
    private BigDecimal startingPrice;

    @NotBlank(message = "카테고리는 필수입니다.")
    @Size(max = 50)
    private String category;

    private MultipartFile thumbnail;

}

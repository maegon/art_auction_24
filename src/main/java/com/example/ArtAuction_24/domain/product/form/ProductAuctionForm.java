package com.example.ArtAuction_24.domain.product.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductAuctionForm {

    @NotBlank(message = "작품명은 필수입니다.")
    @Size(max = 200)
    private String title;

    @NotNull(message = "시작 시간은 필수 항목입니다.")
    private LocalDateTime auctionStartDate; // 시작 일

    @NotNull(message = "마감 시간은 필수 항목입니다.")
    private LocalDateTime auctionEndDate;

}

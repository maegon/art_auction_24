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

    public boolean isProductStartDateValid() {
        if (auctionStartDate == null) {
            return false;
        }

        // 현재 시간을 가져오고, 초와 나노초를 0으로 설정하여 분 단위까지만 비교
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        // startDate가 현재 시각과 같거나 이후인지 확인
        return !auctionStartDate.isBefore(now);
    }

    // 마감 시간이 시작 시간 이후인지 확인하는 유효성 검사
    public boolean isProductEndDateValid() {
        if (auctionStartDate == null || auctionEndDate == null) {
            return false; //
        }
        return auctionEndDate.isAfter(auctionStartDate);
    }

}

package com.example.ArtAuction_24.domain.auction.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionForm {
    @NotBlank(message = "경매 제목은 필수 항목입니다.")
    @Size(max = 200, message = "경매 제목은 200자 이하로 입력해 주세요.")
    private String name;

    @NotNull(message = "시작 시간은 필수 항목입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "마감 시간은 필수 항목입니다.")
    private LocalDateTime endDate;


    @NotEmpty(message = "경매 품목은 필수 항목입니다.")
    private List<Long> products; // 제품 ID 리스트로 변경

    // 시작 시간이 현재 시간 이후인지 확인
    public boolean isStartDateValid() {
        if (startDate == null) {
            return false;
        }

        // 현재 시간을 가져오고, 초와 나노초를 0으로 설정하여 분 단위까지만 비교
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        // startDate가 현재 시각과 같거나 이후인지 확인
        return !startDate.isBefore(now);
    }

    // 마감 시간이 시작 시간 이후인지 확인하는 유효성 검사
    public boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return false; //
        }
        return endDate.isAfter(startDate);
    }
}

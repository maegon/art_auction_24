package com.example.ArtAuction_24.domain.auction.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
}

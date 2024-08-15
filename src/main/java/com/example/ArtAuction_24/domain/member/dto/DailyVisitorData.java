package com.example.ArtAuction_24.domain.member.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class DailyVisitorData {
    private String date;
    private int count;
}
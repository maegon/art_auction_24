package com.example.ArtAuction_24.global.email;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailResponseDto {
    private String email;

    private String code;
    public EmailResponseDto(String code) {
        this.code = code;
    }
}

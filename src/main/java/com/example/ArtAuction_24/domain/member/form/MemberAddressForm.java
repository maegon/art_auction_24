package com.example.ArtAuction_24.domain.member.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MemberAddressForm {
    @NotNull
    private String address;

    private String address1;
    private String address2;
    private String address3;
}

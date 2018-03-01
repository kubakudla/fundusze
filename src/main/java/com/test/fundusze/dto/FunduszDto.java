package com.test.fundusze.dto;

import com.test.fundusze.enums.RodzajFunduszuEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FunduszDto {

    @NotNull(message = "{fundusz.id.nie_moze_byc_pusta}")
    private Long id;

    @NotNull(message = "{fundusz.nazwa.nie_moze_byc_pusta}")
    private String nazwa;

    @NotNull(message = "{fundusz.rodzaj.nie_moze_byc_pusty}")
    private RodzajFunduszuEnum rodzaj;

    public FunduszDto() {
    }

    public FunduszDto(Long id, String nazwa, RodzajFunduszuEnum rodzaj) {
        this.id = id;
        this.nazwa = nazwa;
        this.rodzaj = rodzaj;
    }
}

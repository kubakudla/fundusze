package com.test.fundusze.dto.response;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.enums.RodzajFunduszuEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Podzial {

    private Long id;

    private String nazwa;

    private RodzajFunduszuEnum rodzaj;

    private BigDecimal kwota;

    private BigDecimal procent;

    public Podzial(FunduszDto funduszDto, BigDecimal kwota) {
        this.id = funduszDto.getId();
        this.nazwa = funduszDto.getNazwa();
        this.rodzaj = funduszDto.getRodzaj();
        this.kwota = kwota;
    }
}

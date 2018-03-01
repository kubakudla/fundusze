package com.test.fundusze.dto.request;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.enums.StylInwestowaniaEnum;
import com.test.fundusze.validator.FunduszeConstraint;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class InwestycjaRequest {

    @Min(value = 0, message = "{kwota.wieksza_niz_0}")
    @NotNull(message = "{kwota.nie_moze_byc_pusta}")
    @Digits(fraction = 0, integer = 10, message = "{kwota.liczbaCalkowita}")
    private BigDecimal kwota;

    @NotNull(message = "{styl_inwestowania.nie_moze_byc_pusty}")
    private StylInwestowaniaEnum stylInwestowania;

    @Valid
    @NotNull(message = "{fundusze.nie_moga_byc_puste}")
    @FunduszeConstraint
    @Size(min = 3, message = "{fundusze.musza_byc_min_3}")
    private List<FunduszDto> fundusze;
}

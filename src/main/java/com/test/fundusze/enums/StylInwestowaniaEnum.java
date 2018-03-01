package com.test.fundusze.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum StylInwestowaniaEnum {

    BEZPIECZNY(20, 75, 5), ZROWNOWAZONY(30, 60, 10), AGRESYWNY(40, 20, 40);

    Map<RodzajFunduszuEnum, BigDecimal> rozklad = new HashMap<>();

    StylInwestowaniaEnum(int polski, int zagraniczny, int pieniezny) {
        rozklad.put(RodzajFunduszuEnum.POLSKI, new BigDecimal(polski));
        rozklad.put(RodzajFunduszuEnum.ZAGRANICZNY, new BigDecimal(zagraniczny));
        rozklad.put(RodzajFunduszuEnum.PIENIEZNY, new BigDecimal(pieniezny));
    }
}

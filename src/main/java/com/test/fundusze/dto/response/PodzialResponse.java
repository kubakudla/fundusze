package com.test.fundusze.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PodzialResponse {
    private List<Podzial> podzialy;

    private BigDecimal kwotaNieprzydzielona;
}

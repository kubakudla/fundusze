package com.test.fundusze.util;

import java.math.BigDecimal;

public class ObliczeniaUtil {
    private static final BigDecimal STO = new BigDecimal(100);
    private static int ZAOKRAGLENIE = BigDecimal.ROUND_DOWN;
    private static int ZERO = 0;
    private static int DWA = 2;

    public static BigDecimal obliczProcentKwoty(BigDecimal kwota, BigDecimal procent) {
        return zaokraglijKwote(kwota.multiply(procent).divide(STO));
    }

    public static BigDecimal obliczWartoscProcentowaDanychKwot(BigDecimal kwota, BigDecimal calkowitaKwotaZainwestowana) {
        BigDecimal procent = zaokraglijProcenty(kwota.multiply(STO)).divide(calkowitaKwotaZainwestowana, BigDecimal.ROUND_DOWN);

        int scale = procent.stripTrailingZeros().scale();
        return (scale < DWA) ? procent.setScale((scale < 0) ? ZERO : scale, ZAOKRAGLENIE) : procent;
    }

    public static BigDecimal podzielKwote(BigDecimal kwota, BigDecimal liczba) {
        return zaokraglijKwote(kwota.divide(liczba, ZAOKRAGLENIE));
    }

    public static BigDecimal odejmijKwote(BigDecimal kwota1, BigDecimal kwota2) {
        return zaokraglijKwote(kwota1.subtract(kwota2));
    }

    public static BigDecimal powiekszKwote(BigDecimal kwota1, BigDecimal kwota2) {
        return zaokraglijKwote(kwota1.add(kwota2));
    }

    private static BigDecimal zaokraglijKwote(BigDecimal kwota) {
        return kwota.setScale(ZERO, ZAOKRAGLENIE);
    }

    private static BigDecimal zaokraglijProcenty(BigDecimal procenty) {
        return procenty.setScale(DWA, ZAOKRAGLENIE);
    }
}

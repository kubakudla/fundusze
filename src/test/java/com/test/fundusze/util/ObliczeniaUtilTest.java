package com.test.fundusze.util;

import org.junit.Test;

import java.math.BigDecimal;

import static com.test.fundusze.util.ObliczeniaUtil.obliczProcentKwoty;
import static com.test.fundusze.util.ObliczeniaUtil.obliczWartoscProcentowaDanychKwot;
import static com.test.fundusze.util.ObliczeniaUtil.odejmijKwote;
import static com.test.fundusze.util.ObliczeniaUtil.podzielKwote;
import static com.test.fundusze.util.ObliczeniaUtil.powiekszKwote;
import static org.junit.Assert.assertEquals;

public class ObliczeniaUtilTest {

    @Test
    public void test_procentKwoty() {
        assertEquals(new BigDecimal("5"), obliczProcentKwoty(new BigDecimal("50"), new BigDecimal("10")));
        assertEquals(new BigDecimal("3"), obliczProcentKwoty(new BigDecimal("100"), new BigDecimal("3")));
        assertEquals(new BigDecimal("13"), obliczProcentKwoty(new BigDecimal("28"), new BigDecimal("49")));
    }

    @Test
    public void test_obliczWartoscProcentowaDanychKwot() {
        assertEquals(new BigDecimal("25"), obliczWartoscProcentowaDanychKwot(new BigDecimal("50"), new BigDecimal("200")));
        assertEquals(new BigDecimal("24.87"), obliczWartoscProcentowaDanychKwot(new BigDecimal("50"), new BigDecimal("201")));
        assertEquals(new BigDecimal("10"), obliczWartoscProcentowaDanychKwot(new BigDecimal("10"), new BigDecimal("100")));
        assertEquals(new BigDecimal("10.1"), obliczWartoscProcentowaDanychKwot(new BigDecimal("10.10"), new BigDecimal("100")));
    }

    @Test
    public void test_podzielKwote() {
        assertEquals(new BigDecimal("6"), podzielKwote(new BigDecimal("25"), new BigDecimal("4")));
        assertEquals(new BigDecimal("10"), podzielKwote(new BigDecimal("50"), new BigDecimal("5")));
        assertEquals(new BigDecimal("33"), podzielKwote(new BigDecimal("100"), new BigDecimal("3")));
        assertEquals(new BigDecimal("7"), podzielKwote(new BigDecimal("15"), new BigDecimal("2")));
    }

    @Test
    public void test_odejmijKwote() {
        assertEquals(new BigDecimal("6"), odejmijKwote(new BigDecimal("15"), new BigDecimal("9")));
        assertEquals(new BigDecimal("6"), odejmijKwote(new BigDecimal("15.3"), new BigDecimal("9.2")));
    }

    @Test
    public void test_powiekszKwote() {
        assertEquals(new BigDecimal("24"), powiekszKwote(new BigDecimal("15"), new BigDecimal("9")));
        assertEquals(new BigDecimal("24"), powiekszKwote(new BigDecimal("15.3"), new BigDecimal("9.2")));
        assertEquals(new BigDecimal("24"), powiekszKwote(new BigDecimal("15.3"), new BigDecimal("9.6")));
    }

}

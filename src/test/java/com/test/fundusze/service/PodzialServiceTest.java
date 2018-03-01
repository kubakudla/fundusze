package com.test.fundusze.service;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.dto.request.InwestycjaRequest;
import com.test.fundusze.dto.response.Podzial;
import com.test.fundusze.dto.response.PodzialResponse;
import com.test.fundusze.enums.RodzajFunduszuEnum;
import com.test.fundusze.enums.StylInwestowaniaEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PodzialServiceTest {

    private static final StylInwestowaniaEnum DEFAULT_STYL = StylInwestowaniaEnum.BEZPIECZNY;
    private static final BigDecimal DEFAULT_KWOTA = new BigDecimal("10000");

    private List<FunduszDto> fundusze;

    private InwestycjaRequest inwestycjaRequest;

    @InjectMocks
    private PodzialService podzialService;

    private void initFundusze() {
        fundusze = new ArrayList<>();
        fundusze.add(new FunduszDto(1l, "polski 1", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(2l, "polski 2", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(3l, "zagraniczny 1", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(4l, "zagraniczny 2", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(5l, "zagraniczny 3", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(6l, "pieniezny 1", RodzajFunduszuEnum.PIENIEZNY));
    }

    private void initInwestycjaRequest() {
        inwestycjaRequest = new InwestycjaRequest();
        inwestycjaRequest.setKwota(DEFAULT_KWOTA);
        inwestycjaRequest.setStylInwestowania(DEFAULT_STYL);
        inwestycjaRequest.setFundusze(fundusze);
    }

    /**
     * PODZIEL INWESTYCJE
     */

    @Test
    public void test_etap1_przyklad1StylBezpieczny() {
        //given
        initFundusze();
        initInwestycjaRequest();

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("0", odpowiedz);

        assertKwotaProcentEquals("1000", "10", podzialy, 0);
        assertKwotaProcentEquals("1000", "10", podzialy, 1);
        assertKwotaProcentEquals("2500", "25", podzialy, 2);
        assertKwotaProcentEquals("2500", "25", podzialy, 3);
        assertKwotaProcentEquals("2500", "25", podzialy, 4);
        assertKwotaProcentEquals("500", "5", podzialy, 5);
    }

    @Test
    public void test_etap1_przyklad1StylZrownowazony() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setStylInwestowania(StylInwestowaniaEnum.ZROWNOWAZONY);

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("0", odpowiedz);

        assertKwotaProcentEquals("1500", "15", podzialy, 0);
        assertKwotaProcentEquals("1500", "15", podzialy, 1);
        assertKwotaProcentEquals("2000", "20", podzialy, 2);
        assertKwotaProcentEquals("2000", "20", podzialy, 3);
        assertKwotaProcentEquals("2000", "20", podzialy, 4);
        assertKwotaProcentEquals("1000", "10", podzialy, 5);
    }

    @Test
    public void test_etap1_przyklad1StylAgresywny() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setStylInwestowania(StylInwestowaniaEnum.AGRESYWNY);

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("0", odpowiedz);

        assertKwotaProcentEquals("2000", "20", podzialy, 0);
        assertKwotaProcentEquals("2000", "20", podzialy, 1);
        assertKwotaProcentEquals("668", "6.68", podzialy, 2);
        assertKwotaProcentEquals("666", "6.66", podzialy, 3);
        assertKwotaProcentEquals("666", "6.66", podzialy, 4);
        assertKwotaProcentEquals("4000", "40", podzialy, 5);
    }

    @Test
    public void test_etap2_przyklad1() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setKwota(new BigDecimal("10001"));

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("1", odpowiedz);

        assertKwotaProcentEquals("1000", "10", podzialy, 0);
        assertKwotaProcentEquals("1000", "10", podzialy, 1);
        assertKwotaProcentEquals("2500", "25", podzialy, 2);
        assertKwotaProcentEquals("2500", "25", podzialy, 3);
        assertKwotaProcentEquals("2500", "25", podzialy, 4);
        assertKwotaProcentEquals("500", "5", podzialy, 5);
    }

    @Test
    public void test_etap2_przyklad2() {
        //given
        fundusze = new ArrayList<>();
        fundusze.add(new FunduszDto(0l, "polski 1", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(1l, "polski 2", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(2l, "polski 3", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(3l, "zagraniczny 1", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(4l, "zagraniczny 2", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(5l, "pieniezny 1", RodzajFunduszuEnum.PIENIEZNY));
        initInwestycjaRequest();

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("0", odpowiedz);

        assertKwotaProcentEquals("668", "6.68", podzialy, 0);
        assertKwotaProcentEquals("666", "6.66", podzialy, 1);
        assertKwotaProcentEquals("666", "6.66", podzialy, 2);
        assertKwotaProcentEquals("3750", "37.5", podzialy, 3);
        assertKwotaProcentEquals("3750", "37.5", podzialy, 4);
        assertKwotaProcentEquals("500", "5", podzialy, 5);
    }

    @Test
    public void test_zaNiskaKwota() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setKwota(new BigDecimal("1"));

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("1", odpowiedz);

        assertKwotaProcentEquals("0", "0", podzialy, 0);
        assertKwotaProcentEquals("0", "0", podzialy, 1);
        assertKwotaProcentEquals("0", "0", podzialy, 2);
        assertKwotaProcentEquals("0", "0", podzialy, 3);
        assertKwotaProcentEquals("0", "0", podzialy, 4);
        assertKwotaProcentEquals("0", "0", podzialy, 5);
    }

    @Test
    public void test_zaNiskaKwota2() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setKwota(new BigDecimal("5"));

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("5", odpowiedz);

        assertKwotaProcentEquals("0", "0", podzialy, 0);
        assertKwotaProcentEquals("0", "0", podzialy, 1);
        assertKwotaProcentEquals("0", "0", podzialy, 2);
        assertKwotaProcentEquals("0", "0", podzialy, 3);
        assertKwotaProcentEquals("0", "0", podzialy, 4);
        assertKwotaProcentEquals("0", "0", podzialy, 5);
    }

    @Test
    public void test_niskaKwota() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setKwota(new BigDecimal("43"));

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("1", odpowiedz);

        assertKwotaProcentEquals("4", "9.52", podzialy, 0);
        assertKwotaProcentEquals("4", "9.52", podzialy, 1);
        assertKwotaProcentEquals("12", "28.57", podzialy, 2);
        assertKwotaProcentEquals("10", "23.8", podzialy, 3);
        assertKwotaProcentEquals("10", "23.8", podzialy, 4);
        assertKwotaProcentEquals("2", "4.76", podzialy, 5);
    }

    @Test
    public void test_wysokaKwota() {
        //given
        initFundusze();
        initInwestycjaRequest();
        inwestycjaRequest.setKwota(new BigDecimal("9857436"));

        //when
        PodzialResponse odpowiedz = podzialService.podzielInwestycje(inwestycjaRequest);
        List<Podzial> podzialy = odpowiedz.getPodzialy();

        //then
        assertEquals(inwestycjaRequest.getFundusze().size(), podzialy.size());

        kwotaNieprzydzielonaRowna("1", odpowiedz);

        assertKwotaProcentEquals("985744", "10", podzialy, 0);
        assertKwotaProcentEquals("985743", "9.99", podzialy, 1);
        assertKwotaProcentEquals("2464359", "25", podzialy, 2);
        assertKwotaProcentEquals("2464359", "25", podzialy, 3);
        assertKwotaProcentEquals("2464359", "25", podzialy, 4);
        assertKwotaProcentEquals("492871", "4.99", podzialy, 5);
    }

    private void kwotaNieprzydzielonaRowna(String kwota, PodzialResponse odpowiedz) {
        assertEquals(new BigDecimal(kwota), odpowiedz.getKwotaNieprzydzielona());
    }

    private void assertKwotaProcentEquals(String oczekiwanaKwota, String oczekiwanyProcent, List<Podzial> podzialRespons, int nr) {
        assertEquals(new BigDecimal(oczekiwanaKwota), podzialRespons.get(nr).getKwota());
        assertEquals(new BigDecimal(oczekiwanyProcent), podzialRespons.get(nr).getProcent());
    }

}

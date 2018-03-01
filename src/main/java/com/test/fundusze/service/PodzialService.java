package com.test.fundusze.service;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.dto.request.InwestycjaRequest;
import com.test.fundusze.dto.response.Podzial;
import com.test.fundusze.dto.response.PodzialResponse;
import com.test.fundusze.enums.RodzajFunduszuEnum;
import com.test.fundusze.enums.StylInwestowaniaEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.test.fundusze.util.ObliczeniaUtil.obliczProcentKwoty;
import static com.test.fundusze.util.ObliczeniaUtil.obliczWartoscProcentowaDanychKwot;
import static com.test.fundusze.util.ObliczeniaUtil.odejmijKwote;
import static com.test.fundusze.util.ObliczeniaUtil.podzielKwote;
import static com.test.fundusze.util.ObliczeniaUtil.powiekszKwote;

@Service
public class PodzialService {

    public PodzialResponse podzielInwestycje(InwestycjaRequest inwestycjaRequest) {

        PodzialResponse podzialResponse = new PodzialResponse();
        List<Podzial> podzialy = new ArrayList<>();

        podzielWszystkieFundusze(inwestycjaRequest, podzialy);
        posortujFunduszePoId(podzialy);

        resetujPodzialyJesliPrzydzielonaKwotaZero(podzialy);
        policzKwoteNiezainwestowanaIProcentyWkladu(inwestycjaRequest, podzialResponse, podzialy);

        podzialResponse.setPodzialy(podzialy);
        return podzialResponse;
    }

    private void podzielWszystkieFundusze(InwestycjaRequest inwestycjaRequest, List<Podzial> podzialy) {
        inwestycjaRequest.getStylInwestowania().getRozklad().keySet().forEach(podzielFunduszeWedleRodzajuIStylu(inwestycjaRequest, podzialy));
    }

    private Consumer<RodzajFunduszuEnum> podzielFunduszeWedleRodzajuIStylu(InwestycjaRequest inwestycjaRequest, List<Podzial> podzialResponse) {
        StylInwestowaniaEnum stylInwestowania = inwestycjaRequest.getStylInwestowania();
        BigDecimal kwota = inwestycjaRequest.getKwota();
        Map<RodzajFunduszuEnum, BigDecimal> rozkladMap = stylInwestowania.getRozklad();
        return rodzaj -> podzialResponse.addAll(podzielFunduszeDanegoRodzaju(kwota, rozkladMap.get(rodzaj), znajdzFunduszeDanegoRodzaju(inwestycjaRequest, rodzaj)));
    }

    private List<FunduszDto> znajdzFunduszeDanegoRodzaju(InwestycjaRequest inwestycjaRequest, RodzajFunduszuEnum rodzaj) {
        return inwestycjaRequest.getFundusze().stream().filter(i -> i.getRodzaj().equals(rodzaj)).collect(Collectors.toList());
    }

    private List<Podzial> podzielFunduszeDanegoRodzaju(BigDecimal kwota, BigDecimal procent, List<FunduszDto> fundusze) {
        List<Podzial> podzialy = new ArrayList<>();
        BigDecimal kwotaDostepna = obliczProcentKwoty(kwota, procent);

        mapujFunduszNaPodzial(fundusze, podzialy, kwotaDostepna);
        powiekszKwoteJesliNiewykorzystanoKwotyDostepnej(kwotaDostepna, podzialy);

        return podzialy;
    }

    private void resetujPodzialyJesliPrzydzielonaKwotaZero(List<Podzial> podzialy) {
        //w przypadku, gdy nie uda sie uzyskac podzialu dla chocby jednego funduszu, anulujemy podzial
        if (podzialy.stream().filter(p -> p.getKwota().equals(BigDecimal.ZERO)).count() > 0) {
            podzialy.forEach(p -> p.setKwota(BigDecimal.ZERO));
            podzialy.forEach(p -> p.setProcent(BigDecimal.ZERO));
        }
    }

    private void mapujFunduszNaPodzial(List<FunduszDto> fundusze, List<Podzial> podzialy, BigDecimal kwotaDostepna) {
        fundusze.forEach(f -> podzialy.add(new Podzial(f, podzielKwote(kwotaDostepna, new BigDecimal(fundusze.size())))));
    }

    private void powiekszKwoteJesliNiewykorzystanoKwotyDostepnej(BigDecimal kwotaDostepna, List<Podzial> podzialy) {
        BigDecimal kwotaZainwestowana = BigDecimal.valueOf(podzialy.stream().mapToInt(p -> p.getKwota().intValue()).sum());
        BigDecimal roznica = odejmijKwote(kwotaDostepna, kwotaZainwestowana);
        if (roznica.signum() > 0) {
            posortujFunduszePoId(podzialy);
            //pierwszy fundusz dostaje nadmiar
            Podzial podzial = podzialy.get(0);
            podzial.setKwota(powiekszKwote(podzial.getKwota(), roznica));
        }
    }

    private void posortujFunduszePoId(List<Podzial> podzialy) {
        podzialy.sort(Comparator.comparing(Podzial::getId));
    }

    private void policzKwoteNiezainwestowanaIProcentyWkladu(InwestycjaRequest inwestycjaRequest, PodzialResponse podzialResponse, List<Podzial> podzialy) {
        BigDecimal calkowitaKwotaZainwestowana = BigDecimal.valueOf(podzialy.stream().mapToInt(p -> p.getKwota().intValue()).sum());
        podzialResponse.setKwotaNieprzydzielona(odejmijKwote(inwestycjaRequest.getKwota(), calkowitaKwotaZainwestowana));
        if (calkowitaKwotaZainwestowana.signum() > 0) {
            podzialy.forEach(p -> p.setProcent(obliczWartoscProcentowaDanychKwot(p.getKwota(), calkowitaKwotaZainwestowana)));
        }
    }
}

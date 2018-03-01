package com.test.fundusze.validator;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.dto.request.InwestycjaRequest;
import com.test.fundusze.enums.RodzajFunduszuEnum;
import com.test.fundusze.enums.StylInwestowaniaEnum;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    private static Validator validator;

    private List<FunduszDto> fundusze;

    private InwestycjaRequest inwestycjaRequest;

    private static final StylInwestowaniaEnum DEFAULT_STYL = StylInwestowaniaEnum.AGRESYWNY;
    private static final BigDecimal DEFAULT_KWOTA = new BigDecimal("10000");

    @Before
    public void setUp() {
        initFundusze();
        initInwestycjaRequest();
    }

    private void initFundusze() {
        fundusze = new ArrayList<>();
        fundusze.add(new FunduszDto(1l, "polski 1", RodzajFunduszuEnum.POLSKI));
        fundusze.add(new FunduszDto(2l, "zagraniczny 1", RodzajFunduszuEnum.ZAGRANICZNY));
        fundusze.add(new FunduszDto(3l, "pieniezny 1", RodzajFunduszuEnum.PIENIEZNY));
    }

    private void initInwestycjaRequest() {
        inwestycjaRequest = new InwestycjaRequest();
        inwestycjaRequest.setKwota(DEFAULT_KWOTA);
        inwestycjaRequest.setStylInwestowania(DEFAULT_STYL);
        inwestycjaRequest.setFundusze(fundusze);
    }

    @BeforeClass
    public static void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_validate_nullKwota() {
        //given
        inwestycjaRequest.setKwota(null);

        //then
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);
        ConstraintViolation<InwestycjaRequest> expenseDtoConstraintViolation = violations.stream().findFirst().get();

        //then
        assertEquals("Kwota nie moze byc pusta", expenseDtoConstraintViolation.getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_ujemnaKwota() {
        //given
        inwestycjaRequest.setKwota(BigDecimal.valueOf(-30));

        //then
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);
        ConstraintViolation<InwestycjaRequest> expenseDtoConstraintViolation = violations.stream().findFirst().get();

        //then
        assertEquals("Kwota musi byc wieksza niz 0", expenseDtoConstraintViolation.getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_nieCalkowitaKwota() {
        //given
        inwestycjaRequest.setKwota(BigDecimal.valueOf(1.5));

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Kwota musi byc liczba calkowita"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_nullStylInwestowania() {
        //given
        inwestycjaRequest.setStylInwestowania(null);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);
        ConstraintViolation<InwestycjaRequest> expenseDtoConstraintViolation = violations.stream().findFirst().get();

        //then
        assertEquals("Styl inwestowania nie moze byc pusty", expenseDtoConstraintViolation.getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_emptyFundusze() {
        //given
        inwestycjaRequest.setFundusze(new ArrayList<>());

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc min. 3 fundusze wybrane"));
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertEquals(2, violations.size());
    }

    private boolean findErrorMessage(Set<ConstraintViolation<InwestycjaRequest>> violations, String text) {
        return violations.stream().filter(i -> i.getMessage().equals(text)).findFirst().isPresent();
    }

    @Test
    public void test_validate_zaMaloFunduszy() {
        //given
        inwestycjaRequest.getFundusze().remove(0);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc min. 3 fundusze wybrane"));
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertEquals(2, violations.size());
    }

    @Test
    public void test_validate_zleRodzajeFunduszyBrakPolskiego() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(0);
        funduszDto.setRodzaj(RodzajFunduszuEnum.ZAGRANICZNY);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_zleRodzajeFunduszyBrakZagranicznego() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(1);
        funduszDto.setRodzaj(RodzajFunduszuEnum.PIENIEZNY);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_zleRodzajeFunduszyBrakPienieznego() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(0);
        funduszDto.setRodzaj(RodzajFunduszuEnum.ZAGRANICZNY);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_nullIdFundusze() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(0);
        funduszDto.setId(null);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Id funduszu nie moze byc puste"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_nullNazwaFunduszu() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(0);
        funduszDto.setNazwa(null);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Nazwa funduszu nie moze byc pusta"));
        assertEquals(1, violations.size());
    }

    @Test
    public void test_validate_nullRodzajFundusze() {
        //given
        FunduszDto funduszDto = inwestycjaRequest.getFundusze().get(0);
        funduszDto.setRodzaj(null);

        //when
        Set<ConstraintViolation<InwestycjaRequest>> violations = validator.validate(inwestycjaRequest);

        //then
        assertTrue(findErrorMessage(violations, "Musza byc wybrane fundusze min. 3 rodzajow"));
        assertTrue(findErrorMessage(violations, "Rodzaj funduszu nie moze byc pusty"));
        assertEquals(2, violations.size());
    }
}

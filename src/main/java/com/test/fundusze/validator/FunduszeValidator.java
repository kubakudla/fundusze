package com.test.fundusze.validator;

import com.test.fundusze.dto.FunduszDto;
import com.test.fundusze.enums.RodzajFunduszuEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FunduszeValidator implements ConstraintValidator<FunduszeConstraint, List<FunduszDto>> {
    @Override
    public void initialize(FunduszeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<FunduszDto> fundusze, ConstraintValidatorContext context) {
        return
            czyJestFundusz(fundusze, RodzajFunduszuEnum.POLSKI)
                && czyJestFundusz(fundusze, RodzajFunduszuEnum.PIENIEZNY)
                && czyJestFundusz(fundusze, RodzajFunduszuEnum.ZAGRANICZNY);
    }

    private boolean czyJestFundusz(List<FunduszDto> fundusze, RodzajFunduszuEnum rodzaj) {
        return fundusze.stream().filter(f -> f != null && f.getRodzaj() != null && f.getRodzaj().equals(rodzaj)).count() > 0;
    }
}

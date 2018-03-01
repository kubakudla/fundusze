package com.test.fundusze.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FunduszeValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunduszeConstraint {
    String message() default "{fundusze.musza_byc_3_rodzaje}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

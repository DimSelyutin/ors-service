package com.innowise.swimdom.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OpenCloseTimeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOpenCloseTime {
    String message() default "closeTime must be after openTime";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

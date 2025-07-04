package com.innowise.swimdom.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for valid that close time more than open time.
 */
@Documented
@Constraint(validatedBy = OpenCloseTimeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOpenCloseTime {
    String message() default "closeTime must be after openTime";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.innowise.swimdom.util;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validate booking.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateBooking {

    String message() default "Invalid booking details";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


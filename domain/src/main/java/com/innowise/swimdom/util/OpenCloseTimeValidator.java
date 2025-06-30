package com.innowise.swimdom.util;

import com.innowise.swimdom.entity.PoolWorkingHours;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Class for valid that close time more than open time.
 */
public class OpenCloseTimeValidator implements ConstraintValidator<ValidOpenCloseTime, PoolWorkingHours> {

    /**
     * Method that valid close time, open time.
     *
     * @param entity poolWorkingHours
     * @param context constraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(PoolWorkingHours entity, ConstraintValidatorContext context) {
        if (entity == null) return true;

        if (entity.getOpenTime() == null || entity.getCloseTime() == null) {
            return true;
        }

        return entity.getCloseTime().isAfter(entity.getOpenTime());
    }
}


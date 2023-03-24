package com.happy3friends.toiletmapbackend.validator;

import com.happy3friends.toiletmapbackend.annotation.PaymentTypeAnnotation;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentTypeValidator implements ConstraintValidator<PaymentTypeAnnotation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return false;

        return PaymentTypeEnum.getByValue(s) != null;
    }
}

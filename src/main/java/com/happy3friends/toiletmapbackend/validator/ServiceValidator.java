package com.happy3friends.toiletmapbackend.validator;

import com.happy3friends.toiletmapbackend.annotation.ServiceAnnotation;
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceValidator implements ConstraintValidator<ServiceAnnotation, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return false;

        return ServiceEnum.getByValue(s) != null;
    }
}

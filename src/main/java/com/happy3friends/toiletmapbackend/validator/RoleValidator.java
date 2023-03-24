package com.happy3friends.toiletmapbackend.validator;

import com.happy3friends.toiletmapbackend.annotation.RoleAnnotation;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<RoleAnnotation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return false;

        return RoleEnum.getByValue(s) != null;
    }
}

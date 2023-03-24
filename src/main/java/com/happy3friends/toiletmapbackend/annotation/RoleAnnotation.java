package com.happy3friends.toiletmapbackend.annotation;

import com.happy3friends.toiletmapbackend.validator.RoleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAnnotation {
    String message() default "Invalid role name!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

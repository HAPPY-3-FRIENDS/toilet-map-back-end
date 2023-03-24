package com.happy3friends.toiletmapbackend.annotation;

import com.happy3friends.toiletmapbackend.validator.ServiceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ServiceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceAnnotation {
    String message() default "Invalid service name!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

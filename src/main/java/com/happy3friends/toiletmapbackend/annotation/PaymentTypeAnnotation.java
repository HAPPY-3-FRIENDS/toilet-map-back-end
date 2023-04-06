package com.happy3friends.toiletmapbackend.annotation;

import com.happy3friends.toiletmapbackend.validator.PaymentTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PaymentTypeValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PaymentTypeAnnotation {
    String message() default "Invalid payment method name!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

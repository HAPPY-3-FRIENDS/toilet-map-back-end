package com.happy3friends.toiletmapbackend.request;

import com.happy3friends.toiletmapbackend.annotation.PaymentTypeAnnotation;
import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;

@Getter
public class PaymentRequest {
    private int accountId;
    @PositiveOrZero
    private int total;
    @PaymentTypeAnnotation
    private String method;
}

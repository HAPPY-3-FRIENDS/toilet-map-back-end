package com.happy3friends.toiletmapbackend.request;

import com.happy3friends.toiletmapbackend.annotation.PaymentTypeAnnotation;
import lombok.Getter;

@Getter
public class OrderRequest {
    private int accountId;
    private int comboId;
    @PaymentTypeAnnotation
    private String paymentMethod;
}

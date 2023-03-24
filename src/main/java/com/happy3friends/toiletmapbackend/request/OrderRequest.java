package com.happy3friends.toiletmapbackend.request;

import com.happy3friends.toiletmapbackend.annotation.PaymentTypeAnnotation;
import lombok.Getter;

@Getter
public class OrderRequest {
    private int comboId;
    @PaymentTypeAnnotation
    private String paymentMethod;
}

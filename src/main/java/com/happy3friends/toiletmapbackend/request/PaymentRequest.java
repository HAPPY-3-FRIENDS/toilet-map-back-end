package com.happy3friends.toiletmapbackend.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PaymentRequest {
    private int total;
    @NotBlank
    private String method;
}

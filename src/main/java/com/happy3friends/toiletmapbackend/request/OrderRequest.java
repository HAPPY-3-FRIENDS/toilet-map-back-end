package com.happy3friends.toiletmapbackend.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class OrderRequest {
    private int comboId;
    @NotBlank
    private String paymentMethod;
}

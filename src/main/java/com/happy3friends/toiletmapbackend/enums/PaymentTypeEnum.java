package com.happy3friends.toiletmapbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    BALANCE("Số dư"),
    TURN("Số lượt"),
    CASH("Tiền mặt");

    private final String paymentValue;
    private static final Map<String, PaymentTypeEnum> lookup = new HashMap<>();

    static {
        for (PaymentTypeEnum p : PaymentTypeEnum.values()) {
            lookup.put(p.getPaymentValue(), p);
        }
    }

    public static PaymentTypeEnum get(String paymentTypeValue) {
        return lookup.get(paymentTypeValue);
    }
}

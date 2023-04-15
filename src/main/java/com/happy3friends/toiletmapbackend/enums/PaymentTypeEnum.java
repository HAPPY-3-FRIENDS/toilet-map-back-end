package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    BALANCE(PaymentTypeConstant.BALANCE),
    TURN(PaymentTypeConstant.TURN),
    CASH(PaymentTypeConstant.CASH),
    VN_PAY(PaymentTypeConstant.VN_PAY),
    BANK_TRANSFER(PaymentTypeConstant.BANK_TRANSFER);

    private final String paymentValue;

    private static final Map<String, PaymentTypeEnum> lookup = new HashMap<>();
    static {
        for (PaymentTypeEnum p : PaymentTypeEnum.values()) {
            lookup.put(p.getPaymentValue(), p);
        }
    }

    public static PaymentTypeEnum getByValue(String paymentTypeValue) {
        return lookup.get(paymentTypeValue);
    }

    public static String getByTypeString(String typeString) {
        return PaymentTypeEnum.valueOf(typeString).getPaymentValue();
    }
}

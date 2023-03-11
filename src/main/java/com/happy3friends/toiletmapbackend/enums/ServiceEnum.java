package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.ServiceConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceEnum {
    PEE(ServiceConstant.PEE),
    POOP(ServiceConstant.POOP),
    SHOWER(ServiceConstant.SHOWER);

    private String serviceName;
}

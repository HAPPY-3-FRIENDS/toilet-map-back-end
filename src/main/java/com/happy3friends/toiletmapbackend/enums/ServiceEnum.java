package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.ServiceConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ServiceEnum {
    PEE(ServiceConstant.PEE),
    POOP(ServiceConstant.POOP),
    SHOWER(ServiceConstant.SHOWER);

    private String serviceName;

    private static final Map<String, ServiceEnum> lookup = new HashMap<>();
    static {
        for (ServiceEnum s : ServiceEnum.values()) {
            lookup.put(s.getServiceName(), s);
        }
    }

    public static ServiceEnum getByValue(String serviceName) {
        return lookup.get(serviceName);
    }

    public static String getByTypeString(String typeString) {
        return ServiceEnum.valueOf(typeString).getServiceName();
    }
}

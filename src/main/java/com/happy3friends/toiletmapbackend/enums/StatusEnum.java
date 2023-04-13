package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.StatusConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum StatusEnum {
    ACTIVE(StatusConstant.ACTIVE),
    IN_ACTIVE(StatusConstant.IN_ACTIVE);

    private String status;

    private static final Map<String, StatusEnum> lookup = new HashMap<>();
    static {
        for (StatusEnum s : StatusEnum.values()) {
            lookup.put(s.getStatus(), s);
        }
    }

    public static StatusEnum getByValue(String statusValue) {
        return lookup.get(statusValue);
    }

    public static String getByTypeString(String typeString) {
        return StatusEnum.valueOf(typeString).getStatus();
    }
}

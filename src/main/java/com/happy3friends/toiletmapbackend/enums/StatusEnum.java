package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.StatusConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {
    ACTIVE(StatusConstant.ACTIVE),
    IN_ACTIVE(StatusConstant.IN_ACTIVE);
    private String status;
}

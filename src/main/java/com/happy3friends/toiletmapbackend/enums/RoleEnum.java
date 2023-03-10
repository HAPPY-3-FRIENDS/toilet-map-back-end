package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN(RoleConstant.ADMIN),
    MANAGER(RoleConstant.MANAGER),
    STAFF(RoleConstant.STAFF),
    USER(RoleConstant.USER);

    private int roleId;
}

package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN(RoleConstant.ADMIN),
    MANAGER(RoleConstant.MANAGER),
    STAFF(RoleConstant.STAFF),
    USER(RoleConstant.USER);

    private String roleName;

    private static final Map<String, RoleEnum> lookup = new HashMap<>();
    static {
        for (RoleEnum r : RoleEnum.values()) {
            lookup.put(r.getRoleName(), r);
        }
    }

    public static RoleEnum getByValue(String roleValue) {
        return lookup.get(roleValue);
    }

    public static String getByTypeString(String typeString) {
        return RoleEnum.valueOf(typeString).getRoleName();
    }
}

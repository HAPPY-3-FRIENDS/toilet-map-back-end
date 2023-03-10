package com.happy3friends.toiletmapbackend.enums;

import com.happy3friends.toiletmapbackend.constant.DefaultAccountNameConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DefaultAccountEnum {
    WALK_IN_GUEST(DefaultAccountNameConstant.WALK_IN_GUEST),
    ADMIN(DefaultAccountNameConstant.ADMIN),
    MANAGER(DefaultAccountNameConstant.MANAGER),
    STAFF(DefaultAccountNameConstant.STAFF),
    USER(DefaultAccountNameConstant.USER);

    private String name;
}

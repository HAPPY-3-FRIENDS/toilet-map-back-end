package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.UserInfoResponse;

import java.util.Map;

public interface UserInfoService {
    UserInfoResponse updateUserInfoByFieldsAndAccountId(int accountId, Map<String, Object> fields);
}

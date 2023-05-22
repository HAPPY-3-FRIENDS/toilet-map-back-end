package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.request.UpdatePasswordRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.response.UpdateAccountResponse;

import java.util.Map;

public interface AccountService {
    AccountResponse registerEmployee(AccountRequest accountRequest);
    TokenDTO registerUser(AccountRequest accountRequest);

    UpdateAccountResponse updateAccount(int id, Map<String, Object> fields);

    UpdateAccountResponse updatePassword(int id, UpdatePasswordRequest request);
}

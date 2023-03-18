package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
}

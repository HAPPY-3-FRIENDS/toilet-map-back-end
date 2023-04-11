package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.CompanyResponse;

public interface CompanyService {
    CompanyResponse getCompanyByAccountId(int accountId);
}

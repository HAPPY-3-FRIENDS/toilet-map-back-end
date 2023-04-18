package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;

public interface CompanyService {
    CompanyResponse getCompanyByAccountId(int accountId);

    void createCompany(CompanyCreateRequest request);

    void updateCompany(Integer id, CompanyCreateRequest request);
}

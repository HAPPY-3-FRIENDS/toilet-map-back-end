package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.response.UpdateCompanyResponse;

import java.util.Map;

public interface CompanyService {
    CompanyResponse getCompanyByAccountId(int accountId);

    void createCompany(CompanyCreateRequest request);

    UpdateCompanyResponse updateCompany(Integer id, Map<String, Object> fields);
}

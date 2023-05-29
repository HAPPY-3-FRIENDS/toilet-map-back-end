package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyHasStatusResponse;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    CompanyResponse getCompanyByAccountId(int accountId);

    void createCompany(CompanyCreateRequest request);

    CompanyHasStatusResponse updateCompany(Integer id, Map<String, Object> fields);

    List<CompanyHasStatusResponse> getAllCompanies(BasePaginationRequest paginationRequest);

    int count();

    List<CompanyHasStatusResponse> searchCompany(String searchText, BasePaginationRequest paginationRequest);

    int countSearchingCompanies(String searchText);
}

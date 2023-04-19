package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.response.UpdateCompanyResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CompanyMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CompanyResponse convertCompanyEntityToCompanyResponse(CompanyEntity companyEntity) {
        return Objects.isNull(companyEntity)
                ? null
                : modelMapper.map(companyEntity, CompanyResponse.class);
    }

    public CompanyEntity convertCompanyCreateRequestToCompanyEntity(CompanyCreateRequest request) {
        return Objects.isNull(request)
                ? null
                : modelMapper.map(request, CompanyEntity.class);
    }

    public UpdateCompanyResponse convertAccountEntityToUpdateCompanyResponse(AccountEntity accountEntity) {
        return Objects.isNull(accountEntity)
                ? null
                : modelMapper.map(accountEntity, UpdateCompanyResponse.class);
    }
}

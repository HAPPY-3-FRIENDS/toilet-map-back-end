package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
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
}

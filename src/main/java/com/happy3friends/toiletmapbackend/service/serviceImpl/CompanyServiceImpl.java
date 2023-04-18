package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CompanyMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public CompanyResponse getCompanyByAccountId(int accountId) {

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        // Validate Role
        if (accountEntity.get().getRoleByRoleId().getName().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_ROLE, ToiletMapErrorCodeEnum.INVALID_ROLE.getMessage());

        CompanyEntity companyEntity = companyRepository.getCompanyByAccountId(accountId);

        return companyMapper.convertCompanyEntityToCompanyResponse(companyEntity);
    }

    @Override
    public void createCompany(CompanyCreateRequest request) {
        LOGGER.info("-- Create Company - Start save Company Entity and its information! --");
        CompanyEntity companyEntity = companyMapper.convertCompanyCreateRequestToCompanyEntity(request);
        companyRepository.save(companyEntity);
        LOGGER.info("-- Create Company - Finish save Company Entity and its information! --");
    }

    @Override
    public void updateCompany(Integer id, CompanyCreateRequest request) {
        Optional<CompanyEntity> companyEntity = companyRepository.findById(id);
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        LOGGER.info("-- Update Company - Start save Company Entity and its information! --");
        CompanyEntity entity = companyEntity.get();
        entity.setName(request.getName());
        entity.setLogo(request.getLogo());
        entity.setAddress(request.getAddress());
        entity.setWard(request.getWard());
        entity.setDistrict(request.getDistrict());
        entity.setProvince(request.getProvince());
        entity.setPhone(request.getPhone());

        companyRepository.save(entity);
        LOGGER.info("-- Update Company - Finish save Company Entity and its information! --");

    }
}

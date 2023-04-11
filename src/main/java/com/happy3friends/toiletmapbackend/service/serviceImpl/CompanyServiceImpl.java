package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CompanyMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public CompanyResponse getCompanyByAccountId(int accountId) {

        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException("Account", "Id", accountId);
        if (accountEntity.get().getRoleByRoleId().getName().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException("Account with invalid role!");

        CompanyEntity companyEntity = companyRepository.getCompanyByAccountId(accountId);

        return companyMapper.convertCompanyEntityToCompanyResponse(companyEntity);
    }
}

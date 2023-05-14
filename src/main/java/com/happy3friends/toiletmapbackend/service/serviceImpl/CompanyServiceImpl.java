package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.StatusConstant;
import com.happy3friends.toiletmapbackend.dto.CompanyHasStatusDTO;
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
import com.happy3friends.toiletmapbackend.response.CompanyHasStatusResponse;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.response.UpdateCompanyResponse;
import com.happy3friends.toiletmapbackend.service.CompanyService;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EntityManager entityManager;

    private LinkedHashMap<Integer, List<CompanyHasStatusDTO>> getMapIdListCompanyHasStatusDTO(
            List<CompanyHasStatusDTO> companyHasStatusDTOS) {

        return companyHasStatusDTOS.stream()
                .collect(Collectors.groupingBy(
                        CompanyHasStatusDTO::getId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    private CompanyHasStatusResponse getCompanyHasStatusResponseFromListCompanyHasStatusDTO(
            List<CompanyHasStatusDTO> customRatingDetailsDTOS) {

        return new CompanyHasStatusResponse(
                customRatingDetailsDTOS.get(0).getId(),
                customRatingDetailsDTOS.get(0).getName(),
                customRatingDetailsDTOS.get(0).getLogo(),
                customRatingDetailsDTOS.get(0).getAddress(),
                customRatingDetailsDTOS.get(0).getWard(),
                customRatingDetailsDTOS.get(0).getDistrict(),
                customRatingDetailsDTOS.get(0).getProvince(),
                customRatingDetailsDTOS.get(0).getPhone(),
                customRatingDetailsDTOS.get(0).getStatus()
        );
    }

    private List<CompanyHasStatusResponse> getListCompanyHasStatusResponseFromListCompanyHasStatusDTO(
            List<CompanyHasStatusDTO> companyHasStatusDTOS
    ) {
        LinkedHashMap<Integer, List<CompanyHasStatusDTO>> mapIdListCompanyHasStatusDTO
                = getMapIdListCompanyHasStatusDTO(companyHasStatusDTOS);

        return mapIdListCompanyHasStatusDTO.values()
                .stream().map(this::getCompanyHasStatusResponseFromListCompanyHasStatusDTO)
                .collect(Collectors.toList());
    }

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
    @Transactional(rollbackFor = { Exception.class })
    public void createCompany(CompanyCreateRequest request) {
        try {
            LOGGER.info("-- Create Company - Start save Company Entity and its information! --");
            CompanyEntity companyEntity = companyMapper.convertCompanyCreateRequestToCompanyEntity(request);
            entityManager.persist(companyEntity);
            int companyId = companyEntity.getId();

            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUsername(request.getUsername());
            accountEntity.setPassword(request.getPassword());
            accountEntity.setStatus(StatusConstant.ACTIVE);
            accountEntity.setCompanyId(companyId);
            //Define constant!!!
            accountEntity.setRoleId(2);
            accountRepository.save(accountEntity);

            LOGGER.info("-- Create Company - Finish save Company Entity and its information! --");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("-- Create Company failed: ", e);
            throw new BadRequestException(ToiletMapErrorCodeEnum.CREATE_COMPANY_ERROR, ToiletMapErrorCodeEnum.CREATE_COMPANY_ERROR.getMessage());
        }
    }

    @Override
    public UpdateCompanyResponse updateCompany(Integer id, Map<String, Object> fields) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(id);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        LOGGER.info("-- Update Company - Start save Company Entity and its information! --");
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(AccountEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, accountEntity.get(), value);
        });

        AccountEntity entity = accountRepository.save(accountEntity.get());
        LOGGER.info("-- Update Company - Finish save Company Entity and its information! --");
        return companyMapper.convertAccountEntityToUpdateCompanyResponse(entity);
    }

    @Override
    public List<CompanyHasStatusResponse> getAllCompanies(BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CompanyHasStatusDTO> result = companyRepository.getAllCompanies(pageable);
        return getListCompanyHasStatusResponseFromListCompanyHasStatusDTO(result);
    }

    @Override
    public int count() {
        return (int) companyRepository.count() - 1;
    }


}

package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.StatusEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.request.UpdatePasswordRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.response.UpdateAccountResponse;
import com.happy3friends.toiletmapbackend.service.AccountService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public AccountResponse registerEmployee(AccountRequest accountRequest) {

        // Validate password
        if (!accountRequest.getPassword().isEmpty()) {
            accountRequest.setPassword(accountRequest.getPassword());
        } else throw new BadRequestException(ToiletMapErrorCodeEnum.EMPTY_PASSWORD, ToiletMapErrorCodeEnum.EMPTY_PASSWORD.getMessage());

        // Validate Company
        if (Integer.valueOf(accountRequest.getCompanyId()) == null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.EMPTY_COMPANY_ID, ToiletMapErrorCodeEnum.EMPTY_COMPANY_ID.getMessage());
        Optional<CompanyEntity> companyEntity = companyRepository.findById(accountRequest.getCompanyId());
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        // Validate Role
        if (RoleEnum.getByValue(accountRequest.getRoleName()) == null || RoleEnum.USER.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_ROLE, ToiletMapErrorCodeEnum.INVALID_ROLE.getMessage());

        // Validate action - Cannot register an Admin Account
        if (RoleEnum.ADMIN.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.CREATE_ACCOUNT_ADMIN_ERROR, ToiletMapErrorCodeEnum.CREATE_ACCOUNT_ADMIN_ERROR.getMessage());

        // Validate action - Admin can only create an Account for Manager, Manager can only create an Account for Toilet
        String jwt = JwtUtil.getJwtFromRequest();
        Claims claims = JwtUtil.getAllClaimsFromToken(jwt);
        String authRole = claims.get("role", String.class);
        if (RoleEnum.ADMIN.getRoleName().equals(authRole) && !RoleEnum.MANAGER.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.ADMIN_CREATE_MANAGER_ONLY,ToiletMapErrorCodeEnum.ADMIN_CREATE_MANAGER_ONLY.getMessage());
        if (RoleEnum.MANAGER.getRoleName().equals(authRole) && !RoleEnum.TOILET.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.MANAGER_CREATE_STAFF_ONLY, ToiletMapErrorCodeEnum.MANAGER_CREATE_STAFF_ONLY.getMessage());

        // Validate username
        if (accountRepository.findByUsername(accountRequest.getUsername()) != null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXISTED_USERNAME, ToiletMapErrorCodeEnum.EXISTED_USERNAME.getMessage());

        // TODO: username regex

        // Save Account Entity
        accountRepository.createAccount(accountRequest.getUsername(),
                accountRequest.getPassword(),
                StatusEnum.ACTIVE.getStatus(),
                accountRequest.getRoleName(),
                accountRequest.getCompanyId());

        return new AccountResponse(accountRequest.getUsername(), StatusEnum.ACTIVE.getStatus(), accountRequest.getRoleName());
    }

    @Override
    public TokenDTO registerUser(AccountRequest accountRequest) {

        // Validate phone
        if (accountRepository.findByUsername(accountRequest.getUsername()) != null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXISTED_PHONE, ToiletMapErrorCodeEnum.EXISTED_PHONE.getMessage());

        // TODO: phone regex

        // Save Account Entity
        accountRequest.setRoleName(RoleEnum.USER.getRoleName());
        accountRepository.createAccount(accountRequest.getUsername(),
                accountRequest.getPassword(),
                StatusEnum.ACTIVE.getStatus(),
                accountRequest.getRoleName(),
                null);

        // Save User Info Entity
        userInfoRepository.createUserInfo(
                accountRequest.getUsername(),
                accountRequest.getFullName(),
                null,
                null,
                10000, // Init 10000đ for account balance and 5 turns for account turn
                5,
                PaymentTypeEnum.TURN.getPaymentValue()
        );

        // Create jwt token and return
        AccountEntity accountEntity = accountRepository.findByUsername(accountRequest.getUsername());
        Date now = DateTimeUtil.getDateNow();
        Date expiryDate = new Date(now.getTime() + JwtUtil.JWT_EXPIRATION);
        return new TokenDTO(Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setSubject(String.valueOf(accountEntity.getId()))
                .signWith(SignatureAlgorithm.HS512, JwtUtil.JWT_SECRET)
                .claim("username", accountRequest.getUsername())
                .claim("role", accountRequest.getRoleName())
                .compact());
    }

    @Override
    public UpdateAccountResponse updateAccount(int id, Map<String, Object> fields) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(id);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        LOGGER.info("-- Update Account - Start save Account Entity and its information! --");
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(AccountEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, accountEntity.get(), value);
        });
        AccountEntity entity = accountRepository.save(accountEntity.get());
        LOGGER.info("-- Update Account - Finish save Account Entity and its information! --");
        return new UpdateAccountResponse(
                entity.getUsername(),
                entity.getPassword(),
                entity.getStatus()
        );
    }

    @Override
    public UpdateAccountResponse updatePassword(int id, UpdatePasswordRequest request) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(id);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        if (!request.getOldPassword().equals(accountEntity.get().getPassword())) {
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_PASSWORD, ToiletMapErrorCodeEnum.INVALID_PASSWORD.getMessage());
        }

        accountEntity.get().setPassword(request.getNewPassword());
        AccountEntity entity = accountRepository.save(accountEntity.get());

        return new UpdateAccountResponse(
                entity.getUsername(),
                entity.getPassword(),
                entity.getStatus()
        );
    }
}

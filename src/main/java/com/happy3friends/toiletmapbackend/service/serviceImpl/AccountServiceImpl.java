package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.StatusEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.service.AccountService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse registerEmployee(AccountRequest accountRequest) {
        if (!accountRequest.getPassword().isEmpty()) {
            accountRequest.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        } else throw new BadRequestException("Password cannot empty!");

        if (Integer.valueOf(accountRequest.getCompanyId()) == null)
            throw new BadRequestException("CompanyId cannot empty!");

        if (RoleEnum.getByValue(accountRequest.getRoleName()) == null || RoleEnum.USER.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException("Invalid role name: '" + accountRequest.getRoleName() + "'!");

        if (RoleEnum.ADMIN.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException("Cannot register an Admin Account!");

        // Admin tạo tk cho Manager, Manager tạo tk cho Staff
        String jwt = JwtUtil.getJwtFromRequest();
        Claims claims = JwtUtil.getAllClaimsFromToken(jwt);
        String authRole = claims.get("role", String.class);
        if (RoleEnum.ADMIN.getRoleName().equals(authRole) && !RoleEnum.MANAGER.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException("Admin cannot create an account for any role except for Manager role!");
        if (RoleEnum.MANAGER.getRoleName().equals(authRole) && !RoleEnum.STAFF.getRoleName().equals(accountRequest.getRoleName()))
            throw new BadRequestException("Manager cannot create an account for any role except for Staff role!");

        if (accountRepository.findByUsername(accountRequest.getUsername()) != null)
            throw new BadRequestException("Username '" + accountRequest.getUsername() + "' is not unique! It's already used by another employee!");

        // TODO: username, password regex

        Optional<CompanyEntity> companyEntity = companyRepository.findById(accountRequest.getCompanyId());
        if (!companyEntity.isPresent()) throw new NotFoundException("Company", "Id", accountRequest.getCompanyId());

        accountRepository.createAccount(accountRequest.getUsername(),
                accountRequest.getPassword(),
                StatusEnum.ACTIVE.getStatus(),
                accountRequest.getRoleName(),
                accountRequest.getCompanyId());

        return new AccountResponse(accountRequest.getUsername(), StatusEnum.ACTIVE.getStatus(), accountRequest.getRoleName());
    }

    @Override
    public TokenDTO registerUser(AccountRequest accountRequest) {
        if (accountRepository.findByUsername(accountRequest.getUsername()) != null)
            throw new BadRequestException("Phone '" + accountRequest.getUsername() + "' is not unique! It's already used by another user!");

        // TODO: phone regex

        accountRequest.setRoleName(RoleEnum.USER.getRoleName());

        accountRepository.createAccount(accountRequest.getUsername(),
                accountRequest.getPassword(),
                StatusEnum.ACTIVE.getStatus(),
                accountRequest.getRoleName(),
                null);

        userInfoRepository.createUserInfo(
                accountRequest.getUsername(),
                accountRequest.getFullName(),
                null,
                null,
                10000, // Init 10000đ for account balance and 5 turns for account turn
                5,
                PaymentTypeEnum.BALANCE.getPaymentValue()
        );

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
}

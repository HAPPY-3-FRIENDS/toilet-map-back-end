package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.StatusEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        if (accountRequest.getPassword() != null) {
            accountRequest.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        }

        if (accountRequest.getRoleName().isEmpty()) {
            accountRequest.setRoleName(RoleEnum.USER.getRoleName());
        } else if (RoleEnum.getByValue(accountRequest.getRoleName()) == null)
            throw new BadRequestException("Invalid role name: '" + accountRequest.getRoleName() + "'!");

        // TODO: unique username

        accountRepository.createAccount(accountRequest.getUsername(),
                accountRequest.getPassword(),
                StatusEnum.ACTIVE.getStatus(),
                accountRequest.getRoleName());

        return new AccountResponse(accountRequest.getUsername(), StatusEnum.ACTIVE.getStatus(), accountRequest.getRoleName());
    }
}

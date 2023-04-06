package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.UserInfoMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.response.UserInfoResponse;
import com.happy3friends.toiletmapbackend.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoResponse updateUserInfoByFieldsAndAccountId(int accountId, Map<String, Object> fields) {
        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findById(accountId);
        if (!userInfoEntity.isPresent())
            throw new NotFoundException("UserInfo", "accountId", accountId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserInfoEntity.class, key);
            field.setAccessible(true);
            if (field.getName().equals("defaultPayment")) {
                if (userInfoEntity.get().getDefaultPayment().equals(PaymentTypeEnum.BALANCE.getPaymentValue())) {
                    ReflectionUtils.setField(field, userInfoEntity.get(), PaymentTypeEnum.TURN.getPaymentValue());
                } else if (userInfoEntity.get().getDefaultPayment().equals(PaymentTypeEnum.TURN.getPaymentValue())) {
                    ReflectionUtils.setField(field, userInfoEntity.get(), PaymentTypeEnum.BALANCE.getPaymentValue());
                }
            } else ReflectionUtils.setField(field, userInfoEntity.get(), value);
        });

        // TODO: add turn or balance (not override)
        UserInfoEntity entity = userInfoRepository.save(userInfoEntity.get());

        return userInfoMapper.convertUserInfoEntityToUserInfoResponse(entity);
    }

    @Override
    public UserInfoResponse getUserInfoAccountId(int accountId) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(accountId);

        return userInfoMapper.convertCustomAccountInfoDTOToUserInfoResponse(customAccountInfoDTO);
    }

    @Override
    public UserInfoResponse getUserInfoByAccountUsername(String accountUsername) {
        AccountEntity accountEntity = accountRepository.findByUsername(accountUsername);
        if (accountEntity == null) throw new NotFoundException("Account", "Username", accountUsername);

        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByUsername(accountUsername);

        return userInfoMapper.convertCustomAccountInfoDTOToUserInfoResponse(customAccountInfoDTO);
    }
}

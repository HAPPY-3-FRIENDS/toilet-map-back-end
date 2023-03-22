package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.UserInfoMapper;
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
    private ObjectMapper objectMapper;

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

        UserInfoEntity entity = userInfoRepository.save(userInfoEntity.get());

        return userInfoMapper.convertUserInfoEntityToUserInfoResponse(entity);
    }
}

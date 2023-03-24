package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.response.UserInfoResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserInfoMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public UserInfoResponse convertUserInfoEntityToUserInfoResponse(UserInfoEntity userInfoEntity) {
        return Objects.isNull(userInfoEntity)
                ? null
                : modelMapper.map(userInfoEntity, UserInfoResponse.class);
    }

    public UserInfoResponse convertCustomAccountInfoDTOToUserInfoResponse(CustomAccountInfoDTO customAccountInfoDTO) {
        return Objects.isNull(customAccountInfoDTO)
                ? null
                : modelMapper.map(customAccountInfoDTO, UserInfoResponse.class);
    }
}

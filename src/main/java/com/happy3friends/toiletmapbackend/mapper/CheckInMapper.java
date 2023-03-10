package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CheckInMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CheckInEntity convertCheckInRequestToCheckInEntity(CheckInRequest checkInRequest) {
        return Objects.isNull(checkInRequest)
                ? null
                : modelMapper.map(checkInRequest, CheckInEntity.class);
    }

    public CheckInResponse convertCheckInEntityToCheckInResponse(CheckInEntity checkInEntity) {
        return Objects.isNull(checkInEntity)
                ? null
                : modelMapper.map(checkInEntity, CheckInResponse.class);
    }

    public CheckInResponse convertCustomCheckInDTOToCheckInResponse(CustomCheckInDTO customCheckInDTO) {
        return Objects.isNull(customCheckInDTO)
                ? null
                : modelMapper.map(customCheckInDTO, CheckInResponse.class);
    }
}

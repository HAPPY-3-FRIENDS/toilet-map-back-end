package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CheckInMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckInMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public CheckInEntity convertCheckInRequestToCheckInEntity(CheckInRequest checkInRequest) {
        return Objects.isNull(checkInRequest)
                ? null
                : modelMapper.map(checkInRequest, CheckInEntity.class);
    }

    public CheckInResponse convertCheckInEntityToCheckInResponse(CheckInEntity checkInEntity) {
        if (Objects.isNull(checkInEntity))
            return null;

        CheckInResponse checkInResponse = new CheckInResponse();
        try {
            checkInResponse.setFullName(checkInEntity.getAccountByAccountId().getUserInfoById().getFullName());
            checkInResponse.setDateTime(checkInEntity.getDateTime());
            checkInResponse.setServiceName(checkInEntity.getToiletServiceByToiletServiceId().getServiceByServiceId().getName());
            checkInResponse.setPaymentType(checkInEntity.getPaymentType());
            checkInResponse.setBalance(checkInEntity.getBalance());
            checkInResponse.setTurn(checkInEntity.getTurn());
        } catch (Exception ex) {
            LOGGER.error("Exception occurred: ", ex);
        }

        return checkInResponse;
    }

    public CheckInResponse convertCustomCheckInDTOToCheckInResponse(CustomCheckInDTO customCheckInDTO) {
        return Objects.isNull(customCheckInDTO)
                ? null
                : modelMapper.map(customCheckInDTO, CheckInResponse.class);
    }
}

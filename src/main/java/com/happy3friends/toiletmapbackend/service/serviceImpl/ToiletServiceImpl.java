package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckInResponse> toiletCheckInHistoriesByToiletId(int toiletId) {
        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.toiletCheckInHistoriesByToiletId(toiletId);
        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> {
                    CheckInResponse checkInResponse = checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO);
                    checkInResponse.setPaymentType(String.valueOf(PaymentTypeEnum.getByTypeString(checkInResponse.getPaymentType())));

                    return checkInResponse;
                })
                .collect(Collectors.toList());
    }
}

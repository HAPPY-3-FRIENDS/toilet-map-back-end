package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
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
        List<CheckInEntity> checkInEntities = checkInRepository.toiletCheckInHistoriesByToiletId(toiletId);
        return checkInEntities.stream()
                .map(checkInEntity -> checkInMapper.convertCheckInEntityToCheckInResponse(checkInEntity))
                .collect(Collectors.toList());
    }
}

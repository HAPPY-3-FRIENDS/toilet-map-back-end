package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public CheckInEntity checkIn(CheckInRequest checkInRequest) {
        CheckInEntity checkInEntity = checkInMapper.convertCheckInRequestToCheckInEntity(checkInRequest);
        checkInRepository.save(checkInEntity);
        return checkInEntity;
    }
}

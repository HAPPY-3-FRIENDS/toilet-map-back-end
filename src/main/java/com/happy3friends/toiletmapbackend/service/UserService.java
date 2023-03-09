package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;

public interface UserService {
    CheckInEntity checkIn(CheckInRequest checkInRequest);
}

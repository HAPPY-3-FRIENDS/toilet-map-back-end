package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;

public interface UserService {
    CheckInResponse checkIn(int userId, CheckInRequest checkInRequest);
}

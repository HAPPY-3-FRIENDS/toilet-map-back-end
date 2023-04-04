package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;

import java.util.List;

public interface CheckInService {
    List<CheckInResponse> getCheckInHistoriesByToiletId(int toiletId);
    List<CheckInResponse> getCheckInHistoriesByAccountId(int accountId, String paymentMethod);
    CheckInResponse userCheckIn(int toiletId, CheckInRequest checkInRequest);
}

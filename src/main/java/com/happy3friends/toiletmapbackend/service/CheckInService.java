package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.request.WalkInGuestCheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;

import java.util.List;

public interface CheckInService {
    List<CheckInResponse> getCheckInHistoriesByToiletId(int toiletId);
    List<CheckInResponse> getCheckInHistoriesByAccountId(int accountId, String paymentMethod, BasePaginationRequest paginationRequest);
    CheckInResponse userCheckIn(CheckInRequest checkInRequest);
    List<CheckInResponse> walkInGuestCheckIn(WalkInGuestCheckInRequest walkInGuestCheckInRequest);
    int count(Integer accountId, String paymentMethod);
}

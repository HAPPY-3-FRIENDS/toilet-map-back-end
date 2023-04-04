package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;

import java.util.List;

public interface ToiletService {
    List<CheckInResponse> toiletCheckInHistoriesByToiletId(int toiletId);
    CheckInResponse userCheckIn(int toiletId, CheckInRequest checkInRequest);
    List<ToiletDetailsInfoResponse> getAllToilets();
    ToiletDetailsInfoResponse getToiletByAccountId(int accountId);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.CheckInResponse;

import java.util.List;

public interface ToiletService {
    List<CheckInResponse> toiletCheckInHistoriesByToiletId(int toiletId);
}

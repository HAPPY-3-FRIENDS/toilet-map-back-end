package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.response.ToiletResponse;

import java.util.List;

public interface ToiletService {
    List<ToiletResponse> getAllToiletIncludeIdLatitudeLongitude();
    ToiletDetailsInfoResponse getToiletByAccountId(int accountId);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
}

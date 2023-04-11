package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;

import java.util.List;

public interface ToiletService {
    List<ToiletDetailsInfoResponse> getAllToilets(Double currentLatitude, Double currentLongitude);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
}

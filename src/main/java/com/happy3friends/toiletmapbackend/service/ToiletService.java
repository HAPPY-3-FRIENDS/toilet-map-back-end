package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;

import java.util.List;

public interface ToiletService {
    List<ToiletDetailsInfoResponse> getAllToilets();
    ToiletDetailsInfoResponse getToiletByAccountId(int accountId);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;

import java.util.List;

public interface ToiletService {
    List<ToiletDetailsInfoResponse> getAllToilets(
            Integer companyId,
            Double currentLatitude,
            Double currentLongitude,
            BasePaginationRequest paginationRequest);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
    int count(Integer companyId);
    void createToilet(ToiletCreateRequest toiletCreateRequest) throws Exception;
}

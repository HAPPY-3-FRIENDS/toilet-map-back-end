package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.response.ToiletFacilityResponse;
import com.happy3friends.toiletmapbackend.response.UpdateToiletInfoResponse;

import java.util.List;
import java.util.Map;

public interface ToiletService {
    List<ToiletDetailsInfoResponse> getAllToilets(
            Integer companyId,
            Double currentLatitude,
            Double currentLongitude,
            BasePaginationRequest paginationRequest);
    ToiletDetailsInfoResponse getToiletByToiletId(int toiletId);
    int count(Integer companyId);
    void createToilet(ToiletCreateRequest toiletCreateRequest) throws Exception;

    ToiletDetailsInfoResponse getNearestToilet(Double lat, Double lng, String vehicle);

    UpdateToiletInfoResponse updateToiletInfo(Integer id, Map<String, Object> fields);

    List<Integer> getAllToiletId();

    List<ToiletFacilityResponse> getListToiletFacilityByToiletId(int toiletId);
}

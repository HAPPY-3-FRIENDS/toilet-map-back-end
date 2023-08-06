package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.FacilityRequest;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;

import java.util.List;
import java.util.Map;

public interface FacilityService {
    List<FacilityResponse> getAllFacilities();

    List<FacilityResponse> getFacilitiesByType(String type);

    FacilityResponse createFacility(FacilityRequest request);

    void deleteFacility(int id);
    FacilityResponse updateFacility(int facilityId, Map<String, Object> fields);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.FacilityResponse;

import java.util.List;

public interface FacilityService {
    List<FacilityResponse> getAllFacilities();

    List<FacilityResponse> getFacilitiesByType(String type);
}

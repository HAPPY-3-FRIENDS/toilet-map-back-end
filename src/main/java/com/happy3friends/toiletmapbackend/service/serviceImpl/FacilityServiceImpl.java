package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.FacilityEntity;
import com.happy3friends.toiletmapbackend.mapper.FacilityMapper;
import com.happy3friends.toiletmapbackend.repository.FacilityRepository;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;
import com.happy3friends.toiletmapbackend.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private FacilityMapper facilityMapper;

    @Override
    public List<FacilityResponse> getAllFacilities() {

        List<FacilityEntity> facilityEntities = facilityRepository.findAll();

        return facilityEntities.stream()
                .map(entity -> facilityMapper.convertFacilityEntityToFacilityResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityResponse> getFacilitiesByType(String type) {
        List<FacilityEntity> facilityEntities = facilityRepository.findByType(type);
        return facilityEntities.stream()
                .map(entity -> facilityMapper.convertFacilityEntityToFacilityResponse(entity))
                .collect(Collectors.toList());
    }
}

package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.FacilityEntity;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FacilityMapper {

    @Autowired
    private ModelMapper modelMapper;

    public FacilityResponse convertFacilityEntityToFacilityResponse(FacilityEntity facilityEntity) {
        return Objects.isNull(facilityEntity)
                ? null
                : modelMapper.map(facilityEntity, FacilityResponse.class);
    }
}

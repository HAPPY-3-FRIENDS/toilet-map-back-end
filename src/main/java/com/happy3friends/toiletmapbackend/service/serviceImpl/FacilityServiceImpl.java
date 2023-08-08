package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.FacilityEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.FacilityMapper;
import com.happy3friends.toiletmapbackend.repository.FacilityRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletFacilityRepository;
import com.happy3friends.toiletmapbackend.request.FacilityRequest;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;
import com.happy3friends.toiletmapbackend.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private FacilityMapper facilityMapper;

    @Autowired
    private ToiletFacilityRepository toiletFacilityRepository;

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

    @Override
    public FacilityResponse createFacility(FacilityRequest request) {
        FacilityEntity entity = new FacilityEntity();
        entity.setName(request.getName());
        entity.setType(request.getType());

        if (facilityRepository.existsByName(request.getName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXISTED_FACILITY, ToiletMapErrorCodeEnum.EXISTED_FACILITY.getMessage());

        return facilityMapper.convertFacilityEntityToFacilityResponse(facilityRepository.save(entity));
    }

    @Override
    public void deleteFacility(int id) {
        if (toiletFacilityRepository.existsByFacilityId(id))
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXISTED_FACILITY_IN_USE, ToiletMapErrorCodeEnum.EXISTED_FACILITY_IN_USE.getMessage());

        facilityRepository.deleteById(id);
    }

    @Override
    public FacilityResponse updateFacility(int facilityId, Map<String, Object> fields) {
        Optional<FacilityEntity> facilityEntity = facilityRepository.findById(facilityId);
        if (facilityEntity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_FACILITY, ToiletMapErrorCodeEnum.NOT_FOUND_FACILITY.getMessage());
        if (facilityEntity.get().getType().equals("Phòng"))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_UPDATED_FACILITY, ToiletMapErrorCodeEnum.INVALID_UPDATED_FACILITY.getMessage());

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(FacilityEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, facilityEntity.get(), value);
        });

        FacilityEntity result = facilityRepository.save(facilityEntity.get());

        return facilityMapper.convertFacilityEntityToFacilityResponse(result);
    }

    @Override
    public FacilityResponse getFacilityByFacilityId(int facilityId) {
        Optional<FacilityEntity> facilityEntity = facilityRepository.findById(facilityId);
        if (facilityEntity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_FACILITY, ToiletMapErrorCodeEnum.NOT_FOUND_FACILITY.getMessage());

        return facilityMapper.convertFacilityEntityToFacilityResponse(facilityEntity.get());
    }
}

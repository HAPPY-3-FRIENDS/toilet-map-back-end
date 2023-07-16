package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletResponseDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletFacilityEntity;
import com.happy3friends.toiletmapbackend.repository.FacilityRepository;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.response.ToiletFacilityResponse;
import com.happy3friends.toiletmapbackend.response.ToiletResponse;
import com.happy3friends.toiletmapbackend.response.UpdateToiletInfoResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ToiletMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FacilityRepository facilityRepository;

    public ToiletDetailsInfoResponse convertCustomToiletDTOToToiletDetailsInfoResponse(CustomToiletDTO customToiletDTO) {
        return Objects.isNull(customToiletDTO)
                ? null
                : modelMapper.map(customToiletDTO, ToiletDetailsInfoResponse.class);
    }

    public ToiletDetailsInfoResponse convertCustomToiletDetailsInfoDTOToToiletDetailsInfoResponse(CustomToiletDetailsInfoDTO customToiletDetailsInfoDTO) {
        return Objects.isNull(customToiletDetailsInfoDTO)
                ? null
                : modelMapper.map(customToiletDetailsInfoDTO, ToiletDetailsInfoResponse.class);
    }

    public ToiletEntity convertToiletCreateRequestToiletEntity(ToiletCreateRequest toiletCreateRequest) {
        return Objects.isNull(toiletCreateRequest)
                ? null
                : modelMapper.map(toiletCreateRequest, ToiletEntity.class);
    }

    public UpdateToiletInfoResponse convertToiletEntityToUpdateToiletInfoResponse(ToiletEntity toiletEntity) {
        UpdateToiletInfoResponse result = new UpdateToiletInfoResponse();
        result.setId(toiletEntity.getId());
        result.setName(toiletEntity.getName());
        result.setAddress(toiletEntity.getAddress());
        result.setWard(toiletEntity.getWard());
        result.setDistrict(toiletEntity.getDistrict());
        result.setProvince(toiletEntity.getProvince());
        result.setOpenTime(toiletEntity.getOpenTime().toString());
        result.setCloseTime(toiletEntity.getCloseTime().toString());

        List<String> toiletImagesById = toiletEntity.getToiletImagesById().stream()
                        .map(s -> s.getImageSource())
                        .collect(Collectors.toList());
        result.setToiletImagesById(toiletImagesById);

        List<ToiletFacilityDTO> toiletFacilityDTOS = toiletEntity.getToiletFacilitiesById().stream()
                        .map(s -> new ToiletFacilityDTO(s.getFacilityId(),
                                facilityRepository.findById(s.getFacilityId()).get().getName(),
                                facilityRepository.findById(s.getFacilityId()).get().getType(),
                                s.getQuantity(),
                                s.getDescription()
                        ))
                .collect(Collectors.toList());
        result.setToiletFacilitiesById(toiletFacilityDTOS);

        result.setFree(toiletEntity.isFree());
        result.setStatus(toiletEntity.getStatus());

        return result;
    }

    public ToiletFacilityResponse convertToiletFacilityEntityToToiletFacilityResponse(ToiletFacilityEntity toiletFacilityEntity) {
        return Objects.isNull(toiletFacilityEntity)
                ? null
                : modelMapper.map(toiletFacilityEntity, ToiletFacilityResponse.class);
    }

    public ToiletResponse convertCustomToiletResponseDTOToToiletResponse(CustomToiletResponseDTO customToiletResponseDTO) {
        return Objects.isNull(customToiletResponseDTO)
                ? null
                : modelMapper.map(customToiletResponseDTO, ToiletResponse.class);
    }
}

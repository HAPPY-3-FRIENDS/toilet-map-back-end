package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.ToiletConstant;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ToiletMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ToiletMapper toiletMapper;

    public LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> getMapIdListCustomToiletDetailsInfoDTO(
            List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {

        return customToiletDetailsInfoDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomToiletDetailsInfoDTO::getId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    private ToiletDetailsInfoResponse getToiletFromListCustomToiletDetailsInfoDTOS(
            List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {
        List<ToiletFacilityDTO> toiletFacilityDTOS = customToiletDetailsInfoDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(CustomToiletDetailsInfoDTO::getFacilityName))
                .map(dto -> {
                    ToiletFacilityDTO toiletFacilityDTO = new ToiletFacilityDTO();
                    toiletFacilityDTO.setFacilityName(dto.getFacilityName());
                    toiletFacilityDTO.setFacilityType(dto.getFacilityType());
                    toiletFacilityDTO.setQuantity(dto.getFacilityQuantity());
                    toiletFacilityDTO.setDescription(dto.getFacilityDescription());
                    return toiletFacilityDTO;
                })
                .sorted((o1, o2) -> {
                    if(Objects.equals(o1.getFacilityType(), o2.getFacilityType()))
                        return o1.getFacilityName().compareTo(o2.getFacilityName());
                    else if(o1.getFacilityType().compareTo(o2.getFacilityType()) > 0)
                        return 1;
                    else return -1;
                })
                .collect(Collectors.toList());

        List<String> toiletImageSources = customToiletDetailsInfoDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(CustomToiletDetailsInfoDTO::getToiletImage))
                .map(dto -> dto.getToiletImage())
                .collect(Collectors.toList());

        ToiletDetailsInfoResponse response = customToiletDetailsInfoDTOS.stream()
                .map(dto -> {
                    ToiletDetailsInfoResponse toiletDetailsInfoResponse = new ToiletDetailsInfoResponse();
                    toiletDetailsInfoResponse.setId(dto.getId());
                    toiletDetailsInfoResponse.setToiletName(dto.getToiletName());
                    toiletDetailsInfoResponse.setAddress(dto.getAddress());
                    toiletDetailsInfoResponse.setWard(dto.getWard());
                    toiletDetailsInfoResponse.setDistrict(dto.getDistrict());
                    toiletDetailsInfoResponse.setProvince(dto.getProvince());
                    toiletDetailsInfoResponse.setLatitude(dto.getLatitude());
                    toiletDetailsInfoResponse.setLongitude(dto.getLongitude());
                    toiletDetailsInfoResponse.setNearBy(dto.getNearBy());
                    toiletDetailsInfoResponse.setOpenTime(dto.getOpenTime());
                    toiletDetailsInfoResponse.setCloseTime(dto.getCloseTime());
                    toiletDetailsInfoResponse.setFree(dto.getIsFree());
                    toiletDetailsInfoResponse.setMinPrice(dto.getMinPrice());
                    toiletDetailsInfoResponse.setMaxPrice(dto.getMaxPrice());
                    toiletDetailsInfoResponse.setRatingStar(dto.getRatingStar());
                    toiletDetailsInfoResponse.setToiletFacilities(toiletFacilityDTOS);
                    toiletDetailsInfoResponse.setToiletImageSources(toiletImageSources);
                    return toiletDetailsInfoResponse;
                })
                .findAny().orElse(null);

        return response;
    }

    public List<ToiletDetailsInfoResponse> getAllToilets() {
        List<CustomToiletDTO> toiletEntities = toiletRepository.getAllToiletsIncludeIdLatitudeLongitude();

        return toiletEntities.stream()
                .map(dto -> toiletMapper.convertCustomToiletDTOToToiletDetailsInfoResponse(dto))
                .collect(Collectors.toList());
    }

    public List<ToiletDetailsInfoResponse> getTop10ToiletsNearByCurrentLocation(Double currentLatitude, Double currentLongitude) {
        Double deviationLatitudeMax = currentLatitude + ToiletConstant.LOCATED_DEVIATION;
        Double deviationLongitudeMax = currentLongitude + ToiletConstant.LOCATED_DEVIATION;
        Double distanceCurrentAndDeviationMax = Math.sqrt(
                Math.pow(currentLatitude - deviationLatitudeMax, 2)
                        + Math.pow(currentLongitude - deviationLongitudeMax, 2)
        );

        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getTop10ToiletsNearByCurrentLocation(currentLatitude,
                currentLongitude,
                distanceCurrentAndDeviationMax);

        LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> mapIdListCustomToiletDetailsInfoDTO
                = getMapIdListCustomToiletDetailsInfoDTO(customToiletDetailsInfoDTOS);

        return mapIdListCustomToiletDetailsInfoDTO.entrySet()
                .stream().map(dto -> getToiletFromListCustomToiletDetailsInfoDTOS(dto.getValue()))
                .collect(Collectors.toList());
    }

    public List<ToiletDetailsInfoResponse> getAllToiletsByCompanyId(Integer companyId, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException("Company", "Id", companyId);

        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getAllToiletsByCompanyId(companyId, pageable);

        return customToiletDetailsInfoDTOS.stream()
                .map(dto -> toiletMapper.convertCustomToiletDetailsInfoDTOToToiletDetailsInfoResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<ToiletDetailsInfoResponse> getAllToilets(
            Integer companyId,
            Double currentLatitude,
            Double currentLongitude,
            BasePaginationRequest paginationRequest) {

        List<ToiletDetailsInfoResponse> responses;

        if (companyId != null) {
            responses = getAllToiletsByCompanyId(companyId, paginationRequest);
        } else if (currentLatitude != null && currentLongitude != null) {
            responses = getTop10ToiletsNearByCurrentLocation(currentLatitude, currentLongitude);
        } else {
            responses = getAllToilets();
        }

        return responses;
    }

    @Override
    public ToiletDetailsInfoResponse getToiletByToiletId(int toiletId) {
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getCustomToiletInfoDTOByToiletId(toiletId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }

    @Override
    public int count(Integer companyId) {

        if (companyId != null) {
            Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
            if (!companyEntity.isPresent())
                throw new NotFoundException("Company", "Id", companyId);

            return (int) toiletRepository.countByCompanyId(companyId);
        }

        return (int) toiletRepository.count();
    }
}
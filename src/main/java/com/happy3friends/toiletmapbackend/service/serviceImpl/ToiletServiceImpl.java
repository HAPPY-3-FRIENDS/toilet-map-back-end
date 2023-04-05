package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.constant.ToiletConstant;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ToiletMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ToiletRepository toiletRepository;

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
                    toiletFacilityDTO.setQuantity(dto.getFacilityQuantity());
                    toiletFacilityDTO.setDescription(dto.getFacilityDescription());
                    return toiletFacilityDTO;
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
                    toiletDetailsInfoResponse.setToiletFacilityDTOS(toiletFacilityDTOS);
                    toiletDetailsInfoResponse.setToiletImageSources(toiletImageSources);
                    return toiletDetailsInfoResponse;
                })
                .findAny().orElse(null);

        return response;
    }

    @Override
    public List<ToiletDetailsInfoResponse> getAllToilets(Double currentLatitude, Double currentLongitude) {
        List<ToiletDetailsInfoResponse> responses = new ArrayList<>();

        if (currentLatitude == null && currentLongitude == null) {
            List<CustomToiletDTO> toiletEntities = toiletRepository.getAllToiletsIncludeIdLatitudeLongitude();
            responses = toiletEntities.stream()
                    .map(dto -> toiletMapper.convertCustomToiletDTOToToiletDetailsInfoResponse(dto))
                    .collect(Collectors.toList());
        } else {
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

            responses = mapIdListCustomToiletDetailsInfoDTO.entrySet()
                    .stream().map(dto -> getToiletFromListCustomToiletDetailsInfoDTOS(dto.getValue()))
                    .collect(Collectors.toList());
        }

        return responses;
    }

    @Override
    public ToiletDetailsInfoResponse getToiletByAccountId(int accountId) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getCustomToiletInfoDTOByAccountId(accountId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }

    @Override
    public ToiletDetailsInfoResponse getToiletByToiletId(int toiletId) {
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getCustomToiletInfoDTOByToiletId(toiletId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }
}
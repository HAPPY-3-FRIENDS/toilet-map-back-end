package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletServiceImpl.class);

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private ToiletServiceRepository toiletServiceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckInResponse> toiletCheckInHistoriesByToiletId(int toiletId) {

        if (!toiletRepository.findById(toiletId).isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.toiletCheckInHistoriesByToiletId(toiletId);

        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO))
                .collect(Collectors.toList());
    }

    @Override
    public CheckInResponse userCheckIn(int toiletId, CheckInRequest checkInRequest) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        //Check if service chosen is contained in toilet (ToiletService)
        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);
        Optional<ToiletServiceEntity> toiletServiceEntity
                = toiletServiceEntities.stream()
                .filter(o -> Objects.equals(checkInRequest.getServiceName(), o.getServiceByServiceId().getName()))
                .findFirst();

        if (toiletServiceEntity.isPresent()) {
            int accountId = checkInRequest.getAccountId();
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);

            if (!accountEntity.isPresent())
                throw new NotFoundException("Account", "Id", accountId);

            // Save CheckInEntity
            String defaultAccountPayment = accountEntity.get().getUserInfoById().getDefaultPayment();
            int accountBalance = accountEntity.get().getUserInfoById().getAccountBalance();
            int accountTurn = accountEntity.get().getUserInfoById().getAccountTurn();
            String serviceName = toiletServiceEntity.get().getServiceByServiceId().getName();
            int servicePrice = toiletServiceEntity.get().getServiceByServiceId().getPrice();
            int serviceTurn = toiletServiceEntity.get().getServiceByServiceId().getTurn();

            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(accountId);
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(DateTimeUtil.convertStringToTimestamp(checkInRequest.getDatetime()));
            checkInEntity.setPaymentMethod(defaultAccountPayment);
            switch (defaultAccountPayment) {
                case PaymentTypeConstant.BALANCE:
                    if (accountBalance < servicePrice)
                        throw new BadRequestException("Your account balance is not enough money for paying service '" + serviceName + "' with price '" + servicePrice + "'! " +
                                "Please change your default payment method or top up your account to use this service!");
                    if (!toiletEntity.get().isFree()) {
                        userInfoRepository.updateAccountBalance(accountId, accountBalance - servicePrice);
                        checkInEntity.setBalance(servicePrice);
                    } else {
                        checkInEntity.setBalance(0);
                    }
                    break;
                default:
                    if (accountTurn < serviceTurn)
                        throw new BadRequestException("Your account turn is not enough turn for paying service '" + serviceName + "' with price '" + serviceTurn + "'! " +
                                "Please change your default payment method or top up your account to use this service!");
                    if (!toiletEntity.get().isFree()) {
                        userInfoRepository.updateAccountTurn(accountId, accountTurn - serviceTurn);
                        checkInEntity.setTurn(serviceTurn);
                    } else {
                        checkInEntity.setTurn(0);
                    }
                    break;
            }
            checkInRepository.save(checkInEntity);

            // Convert checkInEntity to checkInResponse
            checkInEntity.setAccountByAccountId(accountEntity.get());
            checkInEntity.setToiletServiceByToiletServiceId(toiletServiceEntity.get());
            CheckInResponse checkInResponse
                    = checkInMapper.convertCheckInEntityToCheckInResponse(checkInEntity);
            return checkInResponse;
        } else {
            LOGGER.error("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + toiletId + "'!");
            throw new BadRequestException("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + toiletId + "'!");
        }
    }

    @Override
    public List<ToiletDetailsInfoResponse> getAllToilets() {
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS = toiletRepository.getListCustomToiletInfoDTO();

        List<ToiletFacilityDTO> toiletFacilityDTOS = new ArrayList<>();

        List<ToiletDetailsInfoResponse> responses = customToiletDetailsInfoDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(
                        CustomToiletDetailsInfoDTO::getId,
                        CustomToiletDetailsInfoDTO::getToiletImage,
                        CustomToiletDetailsInfoDTO::getFacilityName
                ))
                .map(dto -> {
                    ToiletDetailsInfoResponse toiletDetailsInfoResponse = new ToiletDetailsInfoResponse();
                    toiletDetailsInfoResponse.setId(dto.getId());
                    toiletDetailsInfoResponse.setToiletName(dto.getToiletName());
                    toiletDetailsInfoResponse.setAddress(dto.getAddress());
                    toiletDetailsInfoResponse.setWard(dto.getWard());
                    toiletDetailsInfoResponse.setDistrict(dto.getDistrict());
                    toiletDetailsInfoResponse.setProvince(dto.getProvince());
                    toiletDetailsInfoResponse.setLongitude(dto.getLongitude());
                    toiletDetailsInfoResponse.setLatitude(dto.getLatitude());
                    toiletDetailsInfoResponse.setNearBy(dto.getNearBy());
                    toiletDetailsInfoResponse.setOpenTime(dto.getOpenTime());
                    toiletDetailsInfoResponse.setCloseTime(dto.getCloseTime());
                    toiletDetailsInfoResponse.setFree(dto.getIsFree());
                    toiletDetailsInfoResponse.setMinPrice(dto.getMinPrice());
                    toiletDetailsInfoResponse.setMaxPrice(dto.getMaxPrice());
                    toiletDetailsInfoResponse.setRatingStar(dto.getRatingStar());

                    /*ToiletImageDTO toiletImageDTO = new ToiletImageDTO();
                    toiletImageDTO.setImageSource(dto.getToiletImage());
                    toiletDetailsInfoResponse.setToiletImageDTOS(List.of(toiletImageDTO));*/

                    ToiletFacilityDTO toiletFacilityDTO = new ToiletFacilityDTO();
                    toiletFacilityDTO.setFacilityName(dto.getFacilityName());
                    toiletFacilityDTO.setQuantity(dto.getFacilityQuantity());
                    toiletFacilityDTOS.add(toiletFacilityDTO);

                    toiletDetailsInfoResponse.setToiletFacilityDTOS(toiletFacilityDTOS);

                    return toiletDetailsInfoResponse;
                })
                .collect(Collectors.toList());
        return responses;
    }

    private ToiletDetailsInfoResponse getToiletFromListCustomToiletDetailsInfoDTOS(List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {
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
                    toiletDetailsInfoResponse.setLongitude(dto.getLongitude());
                    toiletDetailsInfoResponse.setLatitude(dto.getLatitude());
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
    public ToiletDetailsInfoResponse getToiletByAccountId(int accountId) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS = toiletRepository.getCustomToiletInfoDTOByAccountId(accountId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }

    @Override
    public ToiletDetailsInfoResponse getToiletByToiletId(int toiletId) {
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS = toiletRepository.getCustomToiletInfoDTOByToiletId(toiletId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }
}

/*
*
* .map(dto -> {
                    ToiletDetailsInfoResponse toiletDetailsInfoResponse = new ToiletDetailsInfoResponse();
                    toiletDetailsInfoResponse.setId(dto.getId());
                    toiletDetailsInfoResponse.setToiletName(dto.getToiletName());
                    toiletDetailsInfoResponse.setAddress(dto.getAddress());
                    toiletDetailsInfoResponse.setWard(dto.getWard());
                    toiletDetailsInfoResponse.setDistrict(dto.getDistrict());
                    toiletDetailsInfoResponse.setProvince(dto.getProvince());
                    toiletDetailsInfoResponse.setLongitude(dto.getLongitude());
                    toiletDetailsInfoResponse.setLatitude(dto.getLatitude());
                    toiletDetailsInfoResponse.setNearBy(dto.getNearBy());
                    toiletDetailsInfoResponse.setOpenTime(dto.getOpenTime());
                    toiletDetailsInfoResponse.setCloseTime(dto.getCloseTime());
                    toiletDetailsInfoResponse.setFree(dto.getIsFree());
                    toiletDetailsInfoResponse.setMinPrice(dto.getMinPrice());
                    toiletDetailsInfoResponse.setMaxPrice(dto.getMaxPrice());

                    List<ToiletImageDTO> toiletImageDTOS = new ArrayList<>();
                    ToiletImageDTO toiletImageDTO = new ToiletImageDTO();
                    toiletImageDTO.setImageSource(dto.getToiletImage());
                    toiletImageDTOS.add(toiletImageDTO);



                    toiletDetailsInfoResponse.setToiletImageDTOS(toiletImageDTOS);
                    toiletDetailsInfoResponse.setRatingStar(dto.getRatingStar());
                    return toiletDetailsInfoResponse;
                })
* */

package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletServiceRepository;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckInResponse> toiletCheckInHistoriesByToiletId(int toiletId) {

        // TODO: Check if toiletId is existed

        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.toiletCheckInHistoriesByToiletId(toiletId);

        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> {
                    CheckInResponse checkInResponse = checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO);
                    checkInResponse.setPaymentType(String.valueOf(PaymentTypeEnum.getByTypeString(checkInResponse.getPaymentType())));

                    return checkInResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CheckInResponse userCheckIn(int toiletId, CheckInRequest checkInRequest) {

        //Check if service chosen is contained in toilet (ToiletService)
        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);
        Optional<ToiletServiceEntity> toiletServiceEntity
                = toiletServiceEntities.stream()
                .filter(o -> Objects.equals(checkInRequest.getServiceName(), o.getServiceByServiceId().getName()))
                .findFirst();

        if (toiletServiceEntity.isPresent()) {
            AccountEntity accountEntity = accountRepository.findById(checkInRequest.getAccountId());

            // Save CheckInEntity
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(DateTimeUtil.convertStringToTimestamp(checkInRequest.getDatetime()));
            checkInEntity.setPaymentType(accountEntity.getUserInfoById().getDefaultPayment());
            switch (PaymentTypeEnum.getByTypeString(accountEntity.getUserInfoById().getDefaultPayment())) {
                case PaymentTypeConstant.BALANCE:
                    checkInEntity.setBalance(toiletServiceEntity.get().getServiceByServiceId().getPrice());
                    break;
                default:
                    checkInEntity.setTurn(toiletServiceEntity.get().getServiceByServiceId().getTurn());
                    break;
            }
            checkInRepository.save(checkInEntity);

            // Convert checkInEntity to checkInResponse
            checkInEntity.setAccountByAccountId(accountEntity);
            checkInEntity.setToiletServiceByToiletServiceId(toiletServiceEntity.get());
            checkInEntity.setPaymentType(String.valueOf(PaymentTypeEnum.getByTypeString(checkInEntity.getPaymentType())));
            CheckInResponse checkInResponse
                    = checkInMapper.convertCheckInEntityToCheckInResponse(checkInEntity);
            return checkInResponse;

            // TODO: Modify user info in balance or turn field after check-in

        } else {
            LOGGER.error("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + toiletId + "'!");
            throw new BadRequestException("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + toiletId + "'!");
        }
    }
}

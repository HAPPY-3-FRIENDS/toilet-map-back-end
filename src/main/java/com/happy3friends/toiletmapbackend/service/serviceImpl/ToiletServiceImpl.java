package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
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
    private ToiletRepository toiletRepository;

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
        if (ServiceEnum.getByValue(checkInRequest.getServiceName()) == null)
            throw new BadRequestException("Invalid service name: '" + checkInRequest.getServiceName() + "'!");

        if (!toiletRepository.findById(toiletId).isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        //Check if service chosen is contained in toilet (ToiletService)
        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);
        Optional<ToiletServiceEntity> toiletServiceEntity
                = toiletServiceEntities.stream()
                .filter(o -> Objects.equals(checkInRequest.getServiceName(), o.getServiceByServiceId().getName()))
                .findFirst();

        if (toiletServiceEntity.isPresent()) {
            Optional<AccountEntity> accountEntity = accountRepository.findById(checkInRequest.getAccountId());

            if (!accountEntity.isPresent())
                throw new NotFoundException("Account", "Id", checkInRequest.getAccountId());

            // Save CheckInEntity
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(DateTimeUtil.convertStringToTimestamp(checkInRequest.getDatetime()));
            checkInEntity.setPaymentMethod(accountEntity.get().getUserInfoById().getDefaultPayment());
            switch (accountEntity.get().getUserInfoById().getDefaultPayment()) {
                case PaymentTypeConstant.BALANCE:
                    checkInEntity.setBalance(toiletServiceEntity.get().getServiceByServiceId().getPrice());
                    break;
                default:
                    checkInEntity.setTurn(toiletServiceEntity.get().getServiceByServiceId().getTurn());
                    break;
            }
            checkInRepository.save(checkInEntity);

            // Convert checkInEntity to checkInResponse
            checkInEntity.setAccountByAccountId(accountEntity.get());
            checkInEntity.setToiletServiceByToiletServiceId(toiletServiceEntity.get());
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

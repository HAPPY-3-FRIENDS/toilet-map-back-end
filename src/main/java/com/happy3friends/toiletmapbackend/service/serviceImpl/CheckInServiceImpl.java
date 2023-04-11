package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.request.WalkInGuestCheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
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
public class CheckInServiceImpl implements CheckInService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckInServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private ToiletServiceRepository toiletServiceRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckInResponse> getCheckInHistoriesByToiletId(int toiletId) {

        if (!toiletRepository.findById(toiletId).isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.getCheckInHistoriesByToiletId(toiletId);

        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO))
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckInResponse> getCheckInHistoriesByAccountId(int accountId, String paymentMethod, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        // TODO: check paymentMethod valid

        List<CustomCheckInDTO> customCheckInDTOS
                = checkInRepository.getCheckInHistoriesByAccountId(accountId, paymentMethod, pageable);

        return customCheckInDTOS.stream()
                .map(dto -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public CheckInResponse userCheckIn(CheckInRequest checkInRequest) {
        int toiletId = checkInRequest.getToiletId();
        int accountId = checkInRequest.getAccountId();

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
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);

            if (!accountEntity.isPresent())
                throw new NotFoundException("Account", "Id", accountId);

            // Save CheckInEntity
            CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(accountId);
            String defaultAccountPayment = customAccountInfoDTO.getDefaultPayment();
            int accountBalance = customAccountInfoDTO.getAccountBalance();
            int accountTurn = customAccountInfoDTO.getAccountTurn();
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

    public HashMap<String, ToiletServiceEntity> getMapServiceNameAndToiletServiceEntity(
            List<ToiletServiceEntity> toiletServiceEntities) {

        return toiletServiceEntities.stream()
                .collect(HashMap::new,
                        (m, c) -> m.put(c.getServiceByServiceId().getName(), c),
                        (m, u) -> {});
    }

    @Override
    public List<CheckInResponse> walkInGuestCheckIn(WalkInGuestCheckInRequest walkInGuestCheckInRequest) {
        int toiletId = walkInGuestCheckInRequest.getToiletId();
        int accountId = walkInGuestCheckInRequest.getToiletId();

        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException("Account", "Id", accountId);
        if (!accountEntity.get().getRoleByRoleId().getName().equals(RoleEnum.TOILET.getRoleName()))
            throw new BadRequestException("This account-id is not an account-id of a staff");

        List<CheckInRequest> list = new ArrayList<>();
        walkInGuestCheckInRequest.getCheckInRequests().stream()
                .forEach(obj -> {
                    list.addAll(Collections.nCopies(obj.getQuantity(), obj));
                });

        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);
        HashMap<String, ToiletServiceEntity> mapServiceNameAndToiletServiceEntity
                = getMapServiceNameAndToiletServiceEntity(toiletServiceEntities);

        List<CheckInEntity> checkInEntities = list.stream()
                .map(obj -> {
                    CheckInEntity checkInEntity = new CheckInEntity();
                    checkInEntity.setAccountId(accountId);
                    checkInEntity.setToiletServiceId(
                            mapServiceNameAndToiletServiceEntity.get(
                                    obj.getServiceName()
                            ).getId()
                    );
                    checkInEntity.setDateTime(DateTimeUtil.getTimestampNow());
                    checkInEntity.setPaymentMethod(PaymentTypeEnum.CASH.getPaymentValue());
                    checkInEntity.setBalance(
                            mapServiceNameAndToiletServiceEntity.get(
                                    obj.getServiceName()
                            ).getServiceByServiceId()
                                    .getPrice()
                    );
                    checkInEntity.setToiletServiceByToiletServiceId(
                            mapServiceNameAndToiletServiceEntity.get(
                                    obj.getServiceName()
                            )
                    );
                    return checkInEntity;
                })
                .collect(Collectors.toList());

        checkInRepository.saveAll(checkInEntities);

        return checkInEntities.stream()
                .map(checkInEntity -> checkInMapper.convertCheckInEntityToCheckInResponse(checkInEntity))
                .collect(Collectors.toList());
    }

    @Override
    public int count(Integer accountId, String paymentMethod) {

        if (accountId != null) {
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

            // TODO: check paymentMethod valid

            return checkInRepository.countCheckInHistoriesByAccountId(accountId, paymentMethod);
        }

        return (int) checkInRepository.count();
    }
}

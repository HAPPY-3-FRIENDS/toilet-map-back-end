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
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;
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

import java.sql.Timestamp;
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

        // Validate Toilet
        if (!toiletRepository.findById(toiletId).isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        // Get Check-in histories by Toilet ID
        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.getCheckInHistoriesByToiletId(toiletId);

        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO))
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckInResponse> getCheckInHistoriesByAccountId(int accountId, String paymentMethod, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        // TODO: check paymentMethod valid

        // Get Check-in histories by Account ID
        List<CustomCheckInDTO> customCheckInDTOS
                = checkInRepository.getCheckInHistoriesByAccountId(accountId, paymentMethod, pageable);

        return customCheckInDTOS.stream()
                .map(dto -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(dto))
                .collect(Collectors.toList());
    }

    public CheckInResponse userCheckInWithStaticQRCode(CheckInRequest checkInRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(checkInRequest.getToiletId());
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", checkInRequest.getToiletId());

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(checkInRequest.getAccountId());
        if (!accountEntity.isPresent())
            throw new NotFoundException("Account", "Id", checkInRequest.getAccountId());

        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(checkInRequest.getToiletId());
        // Get min service price in Toilet
        Optional<ToiletServiceEntity> toiletServiceEntity = toiletServiceEntities.stream()
                .min(Comparator.comparing(entity -> entity.getServiceByServiceId().getTurn()));

        if (toiletServiceEntity.isPresent()) {

            // Prepare for saving Check-in Entity
            CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(checkInRequest.getAccountId());
            String defaultAccountPayment = customAccountInfoDTO.getDefaultPayment();
            if (!defaultAccountPayment.equals(PaymentTypeEnum.TURN.getPaymentValue()))
                throw new BadRequestException("Default account's payment type of this account is invalid! It's not by turn");
            int accountTurn = customAccountInfoDTO.getAccountTurn();
            String serviceName = toiletServiceEntity.get().getServiceByServiceId().getName();
            int serviceTurn = toiletServiceEntity.get().getServiceByServiceId().getTurn();
            int serviceTurnPrice = toiletServiceEntity.get().getServiceByServiceId().getTurnPrice();

            // Save Check-in Entity
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(DateTimeUtil.getTimestampNow());
            checkInEntity.setPaymentMethod(defaultAccountPayment);
            if (accountTurn < serviceTurn)
                throw new BadRequestException("Your account turn is not enough turn for paying service '" + serviceName + "' with price '" + serviceTurn + "'! " +
                        "Please top up your account to use this service!");
            if (!toiletEntity.get().isFree()) {
                accountTurn = accountTurn - serviceTurn;
                userInfoRepository.updateAccountTurn(checkInRequest.getAccountId(), accountTurn);
                checkInEntity.setTurn(serviceTurn);
                checkInEntity.setTurnPrice(serviceTurnPrice);
            } else {
                checkInEntity.setTurn(0);
            }
            checkInRepository.save(checkInEntity);

            // Return check-in response: phone, fullName, current account balance, current account turn
            CheckInResponse checkInResponse = new CheckInResponse();
            checkInResponse.setUsername(customAccountInfoDTO.getUsername());
            checkInResponse.setFullName(customAccountInfoDTO.getFullName());
            checkInResponse.setBalance(customAccountInfoDTO.getAccountBalance());
            checkInResponse.setTurn(accountTurn);
            checkInResponse.setPaymentMethod(customAccountInfoDTO.getDefaultPayment());
            return checkInResponse;
        } else {
            throw new NotFoundException("List of toilet's services is not found!");
        }
    }

    public CheckInResponse userCheckInWithDynamicQRCode(CheckInRequest checkInRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(checkInRequest.getToiletId());
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", checkInRequest.getToiletId());

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(checkInRequest.getAccountId());
        if (!accountEntity.isPresent())
            throw new NotFoundException("Account", "Id", checkInRequest.getAccountId());

        // Validate Datetime - 3 * 60s | 1 second = 1000 milliseconds
        Timestamp datetime = DateTimeUtil.convertStringToTimestamp(checkInRequest.getDatetime());
        Timestamp currentDatetime = DateTimeUtil.getTimestampNow();
        if ((currentDatetime.getTime() - datetime.getTime()) > 1000 * 3 * 60)
            throw new BadRequestException("Invalid datetime in QR Code!");

        // Validate Service Name
        if (!checkInRequest.getServiceName().equals(ServiceEnum.getByValue(checkInRequest.getServiceName()).getServiceName()))
            throw new BadRequestException("Invalid service name!");

        //Check if service chosen is contained in toilet (ToiletService)
        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(checkInRequest.getToiletId());
        Optional<ToiletServiceEntity> toiletServiceEntity
                = toiletServiceEntities.stream()
                .filter(o -> Objects.equals(checkInRequest.getServiceName(), o.getServiceByServiceId().getName()))
                .findFirst();

        if (toiletServiceEntity.isPresent()) {

            // Prepare for saving Check-in Entity
            CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(checkInRequest.getAccountId());
            String defaultAccountPayment = customAccountInfoDTO.getDefaultPayment();
            int accountBalance = customAccountInfoDTO.getAccountBalance();
            int accountTurn = customAccountInfoDTO.getAccountTurn();
            String serviceName = toiletServiceEntity.get().getServiceByServiceId().getName();
            int servicePrice = toiletServiceEntity.get().getServiceByServiceId().getPrice();
            int serviceTurn = toiletServiceEntity.get().getServiceByServiceId().getTurn();
            int serviceTurnPrice = toiletServiceEntity.get().getServiceByServiceId().getTurnPrice();

            // Save Check-in Entity
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(datetime);
            checkInEntity.setPaymentMethod(defaultAccountPayment);
            switch (defaultAccountPayment) {
                case PaymentTypeConstant.BALANCE:
                    if (accountBalance < servicePrice)
                        throw new BadRequestException("Your account balance is not enough money for paying service '" + serviceName + "' with price '" + servicePrice + "'! " +
                                "Please change your default payment method or top up your account to use this service!");
                    if (!toiletEntity.get().isFree()) {
                        accountBalance = accountBalance - servicePrice;
                        userInfoRepository.updateAccountBalance(checkInRequest.getAccountId(), accountBalance);
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
                        accountTurn = accountTurn - serviceTurn;
                        userInfoRepository.updateAccountTurn(checkInRequest.getAccountId(), accountTurn);
                        checkInEntity.setTurn(serviceTurn);
                        checkInEntity.setTurnPrice(serviceTurnPrice);
                    } else {
                        checkInEntity.setTurn(0);
                    }
                    break;
            }
            checkInRepository.save(checkInEntity);

            // Return check-in response: phone, fullName, current account balance, current account turn
            CheckInResponse checkInResponse = new CheckInResponse();
            checkInResponse.setUsername(customAccountInfoDTO.getUsername());
            checkInResponse.setFullName(customAccountInfoDTO.getFullName());
            checkInResponse.setBalance(accountBalance);
            checkInResponse.setTurn(accountTurn);
            checkInResponse.setPaymentMethod(customAccountInfoDTO.getDefaultPayment());
            return checkInResponse;
        } else {
            LOGGER.error("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + checkInRequest.getToiletId() + "'!");
            throw new BadRequestException("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + checkInRequest.getToiletId() + "'!");
        }
    }

    @Override
    public CheckInResponse userCheckIn(CheckInRequest checkInRequest) {
        // Static QR Code - Physical QR Code
        if (checkInRequest.getServiceName() == null || checkInRequest.getDatetime() == null) {
            return userCheckInWithStaticQRCode(checkInRequest);
        } else { // Dynamic QR Code
            return userCheckInWithDynamicQRCode(checkInRequest);
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

        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        // Validate Account of Staff duty at Toilet
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException("Account", "Id", accountId);
        if (!accountEntity.get().getRoleByRoleId().getName().equals(RoleEnum.TOILET.getRoleName()))
            throw new BadRequestException("This account-id is not an account-id of a staff");

        // Duplicate Walk-in-guest Check-in by Quantity
        List<CheckInRequest> list = new ArrayList<>();
        walkInGuestCheckInRequest.getCheckInRequests().stream()
                .forEach(obj -> {
                    list.addAll(Collections.nCopies(obj.getQuantity(), obj));
                });

        List<ToiletServiceEntity> toiletServiceEntities
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);
        HashMap<String, ToiletServiceEntity> mapServiceNameAndToiletServiceEntity
                = getMapServiceNameAndToiletServiceEntity(toiletServiceEntities);

        // Create list Check-in Entity
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

        // Save list Check-in Entity
        checkInRepository.saveAll(checkInEntities);

        return checkInEntities.stream()
                .map(checkInEntity -> checkInMapper.convertCheckInEntityToCheckInResponse(checkInEntity))
                .collect(Collectors.toList());
    }

    @Override
    public int count(Integer accountId, String paymentMethod) {

        if (accountId != null) {
            // Validate Account
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

            // TODO: check paymentMethod valid

            return checkInRepository.countCheckInHistoriesByAccountId(accountId, paymentMethod);
        }

        return (int) checkInRepository.count();
    }
}

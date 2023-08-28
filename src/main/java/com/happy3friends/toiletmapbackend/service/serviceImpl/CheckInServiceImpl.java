package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.PaymentTypeConstant;
import com.happy3friends.toiletmapbackend.constant.ServiceConstant;
import com.happy3friends.toiletmapbackend.dto.CheckInTopicData;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.*;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.request.WalkInGuestCheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;
import com.happy3friends.toiletmapbackend.response.NumberOfCurrentCheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import com.happy3friends.toiletmapbackend.service.ConfigurationService;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
    private CompanyRepository companyRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    private ToiletService toiletService;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<CheckInResponse> getCheckInHistoriesByToiletId(int toiletId) {

        // Validate Toilet
        if (!toiletRepository.findById(toiletId).isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Get Check-in histories by Toilet ID
        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.getCheckInHistoriesByToiletId(toiletId);

        return customCheckInDTOS.stream()
                .map(customCheckInDTO -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(customCheckInDTO))
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckInResponse> getCheckInHistories(
            Integer companyId,
            Integer accountId,
            String fromStrDate,
            String toStrDate,
            String keyword,
            String paymentMethod,
            BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        if (companyId != null) {
            // Validate Company
            Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
            if (!companyEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());
        }

        if (accountId != null) {
            // Validate Account
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());
        }

        Date fromDate = null;
        Date toDate = null;

        // If fromDate && toDate is null -> Default fromDate and toDate is currentMonth
        if (fromStrDate == null || toStrDate == null) {
            fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
            toDate = DateTimeUtil.getDateNowWithInitialTime(23, 59, 59, 999);
        } else {  // Validate fromDate & toDate
            fromDate = DateTimeUtil.convertStringToDate(fromStrDate, DateTimeConstant.dd_MM_yyyy);
            toDate = DateTimeUtil.addDays(DateTimeUtil.convertStringToDate(toStrDate, DateTimeConstant.dd_MM_yyyy), 1);
            if (fromDate != null && fromDate.after(toDate))
                throw new BadRequestException(ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE, ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE.getMessage());
        }

        // Get Check-in histories
        List<CustomCheckInDTO> customCheckInDTOS
                = checkInRepository.getCheckInHistories(
                        companyId,
                        accountId,
                        fromDate,
                        toDate,
                        keyword,
                        paymentMethod,
                        pageable);

        List<CheckInResponse> result = customCheckInDTOS.stream()
                .map(dto -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(dto))
                .collect(Collectors.toList());

        return result;
    }

    public CheckInResponse userCheckInWithStaticQRCode(CheckInRequest checkInRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(checkInRequest.getToiletId());
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(checkInRequest.getAccountId());
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

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
                defaultAccountPayment = PaymentTypeEnum.TURN.getPaymentValue();
            int accountTurn = customAccountInfoDTO.getAccountTurn();
            int serviceTurn = toiletServiceEntity.get().getServiceByServiceId().getTurn();
            int serviceTurnPrice = toiletServiceEntity.get().getServiceByServiceId().getPrice();

            // Save Check-in Entity
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(DateTimeUtil.getTimestampNow());
            checkInEntity.setPaymentMethod(defaultAccountPayment);
            if (accountTurn < serviceTurn)
                throw new BadRequestException(ToiletMapErrorCodeEnum.ACCOUNT_TURN_NOT_ENOUGH, ToiletMapErrorCodeEnum.ACCOUNT_TURN_NOT_ENOUGH.getMessage());
            if (!toiletEntity.get().isFree()) {
                accountTurn = accountTurn - serviceTurn;
                UserInfoEntity userInfoEntity = new UserInfoEntity();
                userInfoEntity.setAccountId(customAccountInfoDTO.getAccountId());
                userInfoEntity.setFullName(customAccountInfoDTO.getFullName());
                userInfoEntity.setAvatar(customAccountInfoDTO.getAvatar());
                userInfoEntity.setAccountBalance(customAccountInfoDTO.getAccountBalance());
                userInfoEntity.setAccountTurn(accountTurn);
                userInfoEntity.setDefaultPayment(defaultAccountPayment);
                userInfoRepository.save(userInfoEntity);
                checkInEntity.setTurn(serviceTurn);
                checkInEntity.setTurnPrice(serviceTurnPrice);
            } else {
                checkInEntity.setTurn(0);
            }
            CheckInEntity entity = checkInRepository.save(checkInEntity);

            // Return check-in response: phone, fullName, current account balance, current account turn
            CheckInResponse checkInResponse = new CheckInResponse();
            checkInResponse.setUsername(customAccountInfoDTO.getUsername());
            checkInResponse.setFullName(customAccountInfoDTO.getFullName());
            checkInResponse.setAccountBalance(customAccountInfoDTO.getAccountBalance());
            checkInResponse.setAccountTurn(accountTurn);
            checkInResponse.setDefaultPayment(customAccountInfoDTO.getDefaultPayment());
            checkInResponse.setId(entity.getId());
            checkInResponse.setToiletId(toiletServiceEntity.get().getToiletId());
            checkInResponse.setToiletName(toiletServiceEntity.get().getToiletByToiletId().getName());
            checkInResponse.setServiceName(toiletServiceEntity.get().getServiceByServiceId().getName());
            checkInResponse.setDateTime(entity.getDateTime());
            return checkInResponse;
        } else {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_LIST_TOILET_SERVICES, ToiletMapErrorCodeEnum.NOT_FOUND_LIST_TOILET_SERVICES.getMessage());
        }
    }

    public CheckInResponse userCheckInWithDynamicQRCode(CheckInRequest checkInRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(checkInRequest.getToiletId());
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(checkInRequest.getAccountId());
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        // Validate Service Name
        if (!checkInRequest.getServiceName().equals(ServiceEnum.getByValue(checkInRequest.getServiceName()).getServiceName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_SERVICE, ToiletMapErrorCodeEnum.INVALID_SERVICE.getMessage());

        // Validate available
        NumberOfCurrentCheckInResponse numberOfCurrentCheckInResponse = toiletService.getNumberOfCurrentCheckIn(checkInRequest.getToiletId());
        int numNotAvailableRestroom = numberOfCurrentCheckInResponse.getNumNotAvailableRestroom();
        int numberOfRestroom = numberOfCurrentCheckInResponse.getNumberOfRestroom();
        int numNotAvailableBathroom = numberOfCurrentCheckInResponse.getNumNotAvailableBathroom();
        int numberOfBathroom = numberOfCurrentCheckInResponse.getNumberOfBathroom();
        boolean isFullRestroom = false;
        boolean isFullBathroom = false;
        if (numNotAvailableRestroom >= numberOfRestroom) {
            isFullRestroom = true;
        }
        if (numNotAvailableBathroom >= numberOfBathroom) {
            isFullBathroom = true;
        }
        if (isFullRestroom && checkInRequest.getServiceName().equals(ServiceConstant.POOP)) {
            throw new BadRequestException(ToiletMapErrorCodeEnum.TOILET_FULL_RESTROOM, ToiletMapErrorCodeEnum.TOILET_FULL_RESTROOM.getMessage());
        }
        if (isFullBathroom && checkInRequest.getServiceName().equals(ServiceConstant.SHOWER)) {
            throw new BadRequestException(ToiletMapErrorCodeEnum.TOILET_FULL_BATHROOM, ToiletMapErrorCodeEnum.TOILET_FULL_BATHROOM.getMessage());
        }

        // Validate Datetime - 3 * 60s | 1 second = 1000 milliseconds
        Timestamp datetime = DateTimeUtil.convertStringToTimestamp(checkInRequest.getDatetime());
        Timestamp currentDatetime = DateTimeUtil.getTimestampNow();
        if ((currentDatetime.getTime() - datetime.getTime()) > 1000 * 3 * 60)
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXPIRED_QR_CODE, ToiletMapErrorCodeEnum.EXPIRED_QR_CODE.getMessage());

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
            int serviceTurnPrice = toiletServiceEntity.get().getServiceByServiceId().getPrice();

            // Save Check-in Entity
            Timestamp checkout = DateTimeUtil.getTimestampNow();
            CheckInEntity checkInEntity = new CheckInEntity();
            checkInEntity.setAccountId(checkInRequest.getAccountId());
            checkInEntity.setToiletServiceId(toiletServiceEntity.get().getId());
            checkInEntity.setDateTime(datetime);
            checkInEntity.setPaymentMethod(defaultAccountPayment);
            switch (defaultAccountPayment) {
                case PaymentTypeConstant.BALANCE:
                    if (accountBalance < servicePrice) {
                        CheckInTopicData data = new CheckInTopicData();
                        data.setAccountId(checkInRequest.getAccountId());
                        data.setIsAccountTurnNotEnough("false");
                        template.convertAndSend("/topic/check-in", data);
                    }
                    if (accountBalance < servicePrice)
                        throw new BadRequestException(ToiletMapErrorCodeEnum.ACCOUNT_BALANCE_NOT_ENOUGH, ToiletMapErrorCodeEnum.ACCOUNT_BALANCE_NOT_ENOUGH.getMessage());
                    if (!toiletEntity.get().isFree()) {
                        accountBalance = accountBalance - servicePrice;
                        userInfoRepository.updateAccountBalance(checkInRequest.getAccountId(), accountBalance);
                        checkInEntity.setBalance(servicePrice);
                    } else {
                        checkInEntity.setBalance(0);
                    }
                    break;
                default:
                    if (accountTurn < serviceTurn) {
                        CheckInTopicData data = new CheckInTopicData();
                        data.setAccountId(checkInRequest.getAccountId());
                        data.setIsAccountTurnNotEnough("true");
                        template.convertAndSend("/topic/check-in", data);
                    }
                    if (accountTurn < serviceTurn)
                        throw new BadRequestException(ToiletMapErrorCodeEnum.ACCOUNT_TURN_NOT_ENOUGH, ToiletMapErrorCodeEnum.ACCOUNT_BALANCE_NOT_ENOUGH.getMessage());
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
            List<ConfigurationResponse> configurations = configurationService.getAllConfiguration();
            AtomicInteger AVG_TIME_USING_BATH = new AtomicInteger();
            AtomicInteger AVG_TIME_USING_POOP = new AtomicInteger();
            configurations.forEach(configuration -> {
                if (configuration.getId().equals("AVG_TIME_USING_BATH")) {
                    AVG_TIME_USING_BATH.set(configuration.getValue());
                }
                if (configuration.getId().equals("AVG_TIME_USING_POOP")) {
                    AVG_TIME_USING_POOP.set(configuration.getValue());
                }
            });

            if (serviceTurn == 3) {
                checkout.setTime(datetime.getTime() + TimeUnit.MINUTES.toMillis(AVG_TIME_USING_BATH.get()));
                checkInEntity.setCheckoutTime(checkout);
            } else if (serviceTurn == 2) {
                checkout.setTime(datetime.getTime() + TimeUnit.MINUTES.toMillis(AVG_TIME_USING_POOP.get()));
                checkInEntity.setCheckoutTime(checkout);
            }
            CheckInEntity entity = checkInRepository.save(checkInEntity);

            // Return check-in response: phone, fullName, current account balance, current account turn
            CheckInResponse checkInResponse = new CheckInResponse();
            checkInResponse.setUsername(customAccountInfoDTO.getUsername());
            checkInResponse.setFullName(customAccountInfoDTO.getFullName());
            checkInResponse.setAccountBalance(accountBalance);
            checkInResponse.setAccountTurn(accountTurn);
            checkInResponse.setDefaultPayment(customAccountInfoDTO.getDefaultPayment());
            checkInResponse.setId(entity.getId());
            checkInResponse.setToiletId(toiletServiceEntity.get().getToiletId());
            checkInResponse.setToiletName(toiletServiceEntity.get().getToiletByToiletId().getName());
            checkInResponse.setServiceName(toiletServiceEntity.get().getServiceByServiceId().getName());
            // TODO: Bùa
            checkInResponse.setDateTime(DateUtils.addHours(entity.getDateTime(), 7));
            return checkInResponse;
        } else {
            LOGGER.error("Service '" + checkInRequest.getServiceName() + "' is not contained in Toilet with Id '" + checkInRequest.getToiletId() + "'!");
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_SERVICE, ToiletMapErrorCodeEnum.INVALID_SERVICE.getMessage());
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
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Validate Account of Staff duty at Toilet
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());
        if (!accountEntity.get().getRoleByRoleId().getName().equals(RoleEnum.TOILET.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_ROLE, ToiletMapErrorCodeEnum.INVALID_ROLE.getMessage());

        // Validate available
        NumberOfCurrentCheckInResponse numberOfCurrentCheckInResponse = toiletService.getNumberOfCurrentCheckIn(toiletId);
        int numNotAvailableRestroom = numberOfCurrentCheckInResponse.getNumNotAvailableRestroom();
        int numberOfRestroom = numberOfCurrentCheckInResponse.getNumberOfRestroom();
        int numNotAvailableBathroom = numberOfCurrentCheckInResponse.getNumNotAvailableBathroom();
        int numberOfBathroom = numberOfCurrentCheckInResponse.getNumberOfBathroom();

        int numberEmptyRestroom = numberOfRestroom - numNotAvailableRestroom;
        int numberEmptyBathroom = numberOfBathroom - numNotAvailableBathroom;

        walkInGuestCheckInRequest.getCheckInRequests().forEach(checkInRequest -> {
            if (checkInRequest.getServiceName().equals(ServiceConstant.POOP) && checkInRequest.getQuantity() > numberEmptyRestroom) {
                throw new BadRequestException(ToiletMapErrorCodeEnum.RESTROOM_NOT_ENOUGH, ToiletMapErrorCodeEnum.RESTROOM_NOT_ENOUGH.getMessage());
            }
            if (checkInRequest.getServiceName().equals(ServiceConstant.SHOWER) && checkInRequest.getQuantity() > numberEmptyBathroom) {
                throw new BadRequestException(ToiletMapErrorCodeEnum.BATHROOM_NOT_ENOUGH, ToiletMapErrorCodeEnum.BATHROOM_NOT_ENOUGH.getMessage());
            }
        });


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
                    Timestamp now = DateTimeUtil.getTimestampNow();
                    Timestamp checkout = DateTimeUtil.getTimestampNow();
                    checkInEntity.setAccountId(accountId);
                    checkInEntity.setToiletServiceId(
                            mapServiceNameAndToiletServiceEntity.get(
                                    obj.getServiceName()
                            ).getId()
                    );
                    checkInEntity.setDateTime(now);
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

                    List<ConfigurationResponse> configurations = configurationService.getAllConfiguration();
                    AtomicInteger AVG_TIME_USING_BATH = new AtomicInteger();
                    AtomicInteger AVG_TIME_USING_POOP = new AtomicInteger();
                    configurations.forEach(configuration -> {
                        if (configuration.getId().equals("AVG_TIME_USING_BATH")) {
                            AVG_TIME_USING_BATH.set(configuration.getValue());
                        }
                        if (configuration.getId().equals("AVG_TIME_USING_POOP")) {
                            AVG_TIME_USING_POOP.set(configuration.getValue());
                        }
                    });

                    if (mapServiceNameAndToiletServiceEntity.get(obj.getServiceName()).getServiceId() == 3) {
                        checkout.setTime(now.getTime() + TimeUnit.MINUTES.toMillis(AVG_TIME_USING_BATH.get()));
                        checkInEntity.setCheckoutTime(checkout);
                    } else if (mapServiceNameAndToiletServiceEntity.get(obj.getServiceName()).getServiceId() == 2) {
                        checkout.setTime(now.getTime() + TimeUnit.MINUTES.toMillis(AVG_TIME_USING_POOP.get()));
                        checkInEntity.setCheckoutTime(checkout);
                    }
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
    public int count(
            Integer companyId,
            Integer accountId,
            String fromStrDate,
            String toStrDate,
            String keyword,
            String paymentMethod) {

        if (companyId != null) {
            // Validate Company
            Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
            if (!companyEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());
        }

        if (accountId != null) {
            // Validate Account
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());
        }

        Date fromDate = null;
        Date toDate = null;

        // If fromDate && toDate is null -> Default fromDate and toDate is currentMonth
        if (fromStrDate == null || toStrDate == null) {
            fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
            toDate = DateTimeUtil.getDateNowWithInitialTime(23, 59, 59, 999);
        } else {  // Validate fromDate & toDate
            fromDate = DateTimeUtil.convertStringToDate(fromStrDate, DateTimeConstant.dd_MM_yyyy);
            toDate = DateTimeUtil.addDays(DateTimeUtil.convertStringToDate(toStrDate, DateTimeConstant.dd_MM_yyyy), 1);
            if (fromDate != null && fromDate.after(toDate))
                throw new BadRequestException(ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE, ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE.getMessage());
        }

        return checkInRepository.countCheckInHistories(companyId, accountId, fromDate, toDate, keyword, paymentMethod);
    }

    @Override
    public int countCheckInNotRatingYet(Integer accountId, String paymentMethod) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        return checkInRepository.countCheckInNotRatingYetHistoriesByAccountId(accountId, paymentMethod);
    }
}

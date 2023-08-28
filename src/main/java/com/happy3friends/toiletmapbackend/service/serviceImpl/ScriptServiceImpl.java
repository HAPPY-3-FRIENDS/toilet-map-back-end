package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CheckInFullAToiletTotal;
import com.happy3friends.toiletmapbackend.dto.CheckInScriptTotal;
import com.happy3friends.toiletmapbackend.entity.*;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.repository.ServiceRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.CheckInFullAToiletRequest;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.request.CheckInScriptRequest;
import com.happy3friends.toiletmapbackend.request.WalkInGuestCheckInRequest;
import com.happy3friends.toiletmapbackend.response.*;
import com.happy3friends.toiletmapbackend.service.*;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private ToiletService toiletService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private SuggestionMapper suggestionMapper;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<String> random100UserCheckIn() {
        List<UserInfoEntity> listAllUsers = userInfoRepository.findAll();

        List<UserInfoEntity> list100Users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            int index = (int)(Math.random() * listAllUsers.size());
            list100Users.add(listAllUsers.get(index));
            listAllUsers.remove(index);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            String message = process(4, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            result.add(message);
        }
        for (int i = 13; i < 16; i++) {
            String message = process(5, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            result.add(message);
        }
        for (int i = 16; i < 26; i++) {
            String message = process(45, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            result.add(message);
        }
        for (int i = 26; i < 56; i++) {
            String message = process(232, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            result.add(message);
        }
        for (int i = 56; i < 64; i++) {
            String message = process(233, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            result.add(message);
        }
        for (int i = 64; i < 74; i++) {
            String message = process(4, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            result.add(message);
        }
        for (int i = 74; i < 76; i++) {
            String message = process(5, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            result.add(message);
        }
        for (int i = 76; i < 84; i++) {
            String message = process(45, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            result.add(message);
        }
        for (int i = 84; i < 89; i++) {
            String message = process(232, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            result.add(message);
        }
        for (int i = 89; i < 90; i++) {
            String message = process(233, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            result.add(message);
        }
        for (int i = 90; i < 92; i++) {
            String message = process(4, list100Users.get(i).getAccountId(), "Đi tắm");
            result.add(message);
        }
        for (int i = 92; i < 94; i++) {
            String message = process(45, list100Users.get(i).getAccountId(), "Đi tắm");
            result.add(message);
        }
        for (int i = 94; i < 99; i++) {
            String message = process(232, list100Users.get(i).getAccountId(), "Đi tắm");
            result.add(message);
        }
        for (int i = 99; i < 100; i++) {
            String message = process(233, list100Users.get(i).getAccountId(), "Đi tắm");
            result.add(message);
        }

        return result;
    }

    @Override
    public CheckInFullAToiletResponse checkInFullAToilet(CheckInFullAToiletRequest request) {
        CheckInFullAToiletResponse response = new CheckInFullAToiletResponse();
        CheckInFullAToiletTotal checkInFullAToiletTotal = new CheckInFullAToiletTotal();
        int checkInSuccess = 0;
        int checkInFail = 0;

        Date date = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(date) + " 00:00:00";
        String endDate = dateFormat.format(date) + " 23:59:59";

        String now = DateTimeUtil.getTimestampNow().toString();

        ToiletDetailsInfoResponse toiletDetail = toiletService.getToiletByToiletId(request.getToiletId());

        AtomicInteger numberOfBathRoom = new AtomicInteger();
        AtomicInteger numberOfRestRoom = new AtomicInteger();

        toiletDetail.getToiletFacilities().forEach(facility -> {
            if (facility.getFacilityId() == 1) {
                numberOfRestRoom.addAndGet(facility.getQuantity());
            } else if (facility.getFacilityId() == 2) {
                numberOfBathRoom.set(facility.getQuantity());
            } else  if (facility.getFacilityId() == 3) {
                numberOfRestRoom.addAndGet(facility.getQuantity());
            }
        });

        int numberOfAvailableBathroom = numberOfBathRoom.get() - checkInRepository
                .getNumberNotAvailableRoom(request.getToiletId(), 3, startDate, endDate, now);
        int numberOfAvailableRestroom = numberOfRestRoom.get() - checkInRepository
                .getNumberNotAvailableRoom(request.getToiletId(), 2, startDate, endDate, now);

        List<UserInfoEntity> listAll = userInfoRepository.findAll();

        ServiceEntity serviceEntity = serviceRepository.getReferenceById(3);

        List<UserInfoEntity> listAllUsers = listAll.stream()
                .filter(user -> user.getAccountTurn() > serviceEntity.getTurn() && user.getAccountBalance() > serviceEntity.getPrice())
                .collect(Collectors.toList());

        if ((request.getNumberOfRestroom() + request.getNumberOfBathroom()) > listAllUsers.size()) {
            throw new BadRequestException(ToiletMapErrorCodeEnum.NOT_ENOUGH_USER, ToiletMapErrorCodeEnum.NOT_ENOUGH_USER.getMessage() + ", số lượng user đủ tiền trong hệ thống là " + listAllUsers.size() + " người.");
        }

        List<UserInfoEntity> listUsers = new ArrayList<>();

        for (int i = 0; i < request.getNumberOfBathroom() + request.getNumberOfRestroom(); i++) {
            int index = (int)(Math.random() * listAllUsers.size());
            listUsers.add(listAllUsers.get(index));
            listAllUsers.remove(index);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < listUsers.size() - request.getNumberOfRestroom(); i++) {
            String message;
            if (i >= numberOfAvailableBathroom) {
                message = listUsers.get(i).getFullName() + " không thể đi tắm vì hết phòng";
                checkInFail++;
            } else {
                message = process(request.getToiletId(), listUsers.get(i).getAccountId(), "Đi tắm");
                checkInSuccess++;
            }
            result.add(message);
        }
        for (int i = listUsers.size() - request.getNumberOfRestroom(); i < listUsers.size(); i++) {
            String message;
            if (i >= numberOfAvailableRestroom + listUsers.size() - request.getNumberOfRestroom()) {
                message = listUsers.get(i).getFullName() + " không thể đại tiện vì hết phòng";
                checkInFail++;
            } else {
                message = process(request.getToiletId(), listUsers.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
                checkInSuccess++;
            }

            result.add(message);
        }

        NumberOfCurrentCheckInResponse numberOfCurrentCheckInResponse = toiletService.getNumberOfCurrentCheckIn(request.getToiletId());

        checkInFullAToiletTotal.setCheckInSuccess(checkInSuccess);
        checkInFullAToiletTotal.setCheckInFail(checkInFail);
        checkInFullAToiletTotal.setToiletRoomEmpty(numberOfCurrentCheckInResponse.getNumberOfRestroom() - numberOfCurrentCheckInResponse.getNumNotAvailableRestroom());
        checkInFullAToiletTotal.setBathRoomEmpty(numberOfCurrentCheckInResponse.getNumberOfBathroom() - numberOfCurrentCheckInResponse.getNumNotAvailableBathroom());

        response.setListUserCheckIn(result);
        response.setTotal(checkInFullAToiletTotal);

        return response;
    }

    @Override
    public CheckoutResponse checkout(int toiletId) {
        CheckoutResponse response = new CheckoutResponse();
        Date date = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(date) + " 00:00:00";
        String endDate = dateFormat.format(date) + " 23:59:59";

        String now = DateTimeUtil.getTimestampNow().toString();

        List<Integer> list = checkInRepository.getListAvailableCheckIn(toiletId, startDate, endDate, now);

        List<String> result = new ArrayList<>();

        if (list.isEmpty()) {
            result.add("Nhà vệ sinh hiện tại không có người dùng nào để checkout");
            response.setListUserCheckout(result);
            response.setNumberOfUserBath(0);
            response.setNumberOfUserBath(0);
            return response;
        }
        checkInRepository.checkout(toiletId, startDate, endDate, now);

        checkInRepository.getListUserByListCheckInId(list).forEach(u -> {
            result.add(u + " đã checkout");
        });

        List<CheckInEntity> listCheckInEntity = checkInRepository.getCheckInByListCheckInId(list);

        AtomicInteger numberOfUserPoop = new AtomicInteger();
        AtomicInteger numberOfUserBath = new AtomicInteger();
        listCheckInEntity.forEach(checkInEntity -> {
            if (checkInEntity.getTurn() != null) {
                if (checkInEntity.getToiletServiceByToiletServiceId().getServiceId() == 2) {

                    numberOfUserPoop.getAndIncrement();
                }

                if (checkInEntity.getToiletServiceByToiletServiceId().getServiceId() == 3) {
                    numberOfUserBath.getAndIncrement();
                }
            } else {
                if (checkInEntity.getToiletServiceByToiletServiceId().getServiceId() == 2) {
                    numberOfUserPoop.getAndIncrement();
                }

                if (checkInEntity.getToiletServiceByToiletServiceId().getServiceId() == 3) {
                    numberOfUserBath.getAndIncrement();
                }
            }

        });

        response.setListUserCheckout(result);
        response.setNumberOfUserPoop(numberOfUserPoop.get());
        response.setNumberOfUserBath(numberOfUserBath.get());

        return response;
    }

    @Override
    public List<SuggestionSchedulerResponse> runScheduler(String date) throws ParseException {
        List<SuggestionSchedulerResponse> responses = new ArrayList<>();
        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        Date startDate = DateUtils.addMonths(endDate, -3);

        List<ConfigurationResponse> configurationResponses = configurationService.getAllConfiguration();
        AtomicInteger bathTime = new AtomicInteger();
        AtomicInteger poopTime = new AtomicInteger();
        AtomicInteger belowThreshold = new AtomicInteger();
        AtomicInteger overThreshold = new AtomicInteger();

        configurationResponses.forEach(configuration -> {
            if (configuration.getId().equals("AVG_TIME_HAVING_BATH")) {
                bathTime.set(configuration.getValue());
            }
            if (configuration.getId().equals("THRESHOLD_BELOW")) {
                belowThreshold.set(configuration.getValue());
            }
            if (configuration.getId().equals("THRESHOLD_OVER")) {
                overThreshold.set(configuration.getValue());
            }
            if (configuration.getId().equals("AVG_TIME_HAVING_POOP")) {
                poopTime.set(configuration.getValue());
            }
        });

        List<Integer> listToiletId = toiletService.getAllToiletId();
        listToiletId.forEach(toiletId -> {
            List<StatisticForSuggestionResponse> listStatistics = statisticService.getStatisticsByToiletId(toiletId, startDate, endDate);
            StatisticForSuggestionResponse result = listStatistics.get(0);

            List<ToiletFacilityResponse> listToiletFacilities = toiletService.getListToiletFacilityByToiletId(toiletId);

            AtomicInteger numberOfRestroom = new AtomicInteger();
            AtomicInteger numberOfBathroom = new AtomicInteger();
            listToiletFacilities.forEach(toiletFacilityResponse -> {

                if (toiletFacilityResponse.getFacilityId() == 2) {
                    numberOfBathroom.set(toiletFacilityResponse.getQuantity());
                } else {
                    numberOfRestroom.addAndGet(toiletFacilityResponse.getQuantity());
                }
            });
            result.setNumberOfRestroom(numberOfRestroom.get());
            result.setNumberOfBathroom(numberOfBathroom.get());

            double expectedCountMax = result.getHours() * (result.getNumberOfBathroom() * 60 / bathTime.get() + result.getNumberOfRestroom() * 60 / poopTime.get()) * 90;

            int expectedCountMin = (result.getNumberOfBathroom() + result.getNumberOfRestroom()) * belowThreshold.get() * 90;

            SuggestionEntity entity = new SuggestionEntity();
            entity.setToiletId(toiletId);
            entity.setStartDate(new java.sql.Date(startDate.getTime()));
            Date endDateOfQuarter = DateUtils.addDays(endDate, -1);
            entity.setEndDate(new java.sql.Date(endDateOfQuarter.getTime()));
            entity.setActualCount(result.getActualCount());

            Date endDatePrevious = DateUtils.addDays(startDate, -1);
            SuggestionEntity previous = suggestionService.getPreviousQuarterSuggestion(toiletId, endDatePrevious);
            int streak = 1;

            String message = "";
            if (result.getActualCount() >= expectedCountMax * overThreshold.get() / 100) {
                message = "Số lượt đi thực tế vượt " +
                        overThreshold.get() +
                        "% so với sức chứa, gợi ý mở thêm nhà vệ sinh gần đây hoặc mở thêm phòng vệ sinh.";
                entity.setIsLow(false);
                entity.setExpectedCount(expectedCountMax);
                if (previous != null && !previous.getIsLow() && (previous.getIsAccepted() == null || !Boolean.TRUE.equals(previous.getIsAccepted()))) {
                        streak = previous.getStreak() + 1;
                }
                entity.setStreak(streak);
                entity.setMessage(message);
                suggestionService.save(entity);
            }

            if (result.getActualCount() < expectedCountMin) {
                message = "Số lượt đi thực tế dưới " + expectedCountMin + " lượt.";
                entity.setIsLow(true);
                entity.setExpectedCount((double) expectedCountMin);
                if (previous != null && previous.getIsLow()) {
                    streak = previous.getStreak() + 1;
                }
                entity.setStreak(streak);
                entity.setMessage(message);
                suggestionService.save(entity);
            }

            responses.add(suggestionMapper.convertSuggestionEntityToSuggestionSchedulerResponse(entity));
        });
        return responses;
    }

    @Override
    public CheckInScriptResponse randomUserCheckIn(CheckInScriptRequest request) {
        CheckInScriptResponse result = new CheckInScriptResponse();
        List<String> listUserCheckIn = new ArrayList<>();

        List<Integer> listToiletId = request.getListToiletId();

        List<UserInfoEntity> listAll = userInfoRepository.findAll();

        if (request.getNumberOfUser() > listAll.size()) {
            throw new BadRequestException(ToiletMapErrorCodeEnum.NOT_ENOUGH_USER, ToiletMapErrorCodeEnum.NOT_ENOUGH_USER.getMessage() + ", số lượng user trong hệ thống là " + listAll.size() + " người.");
        }

        ServiceEntity serviceEntity = serviceRepository.getReferenceById(3);

        List<UserInfoEntity> listAllUsers = listAll.stream()
                .filter(user -> user.getAccountTurn() > serviceEntity.getTurn() && user.getAccountBalance() > serviceEntity.getPrice())
                .collect(Collectors.toList());

        Random random = new Random();

        List<Integer> listRandomUser = randomList(3, request.getNumberOfUser());
        List<Integer> listRandomGuest = randomList(3, request.getNumberOfGuest());

        int numberOfUserPee = listRandomUser.get(0);
        int numberOfUserPoop = listRandomUser.get(1);
        int numberOfUserTakeAShower = listRandomUser.get(2);
        int numberOfGuestPee = listRandomGuest.get(0);
        int numberOfGuestPoop = listRandomGuest.get(1);
        int numberOfGuestTakeAShower = listRandomGuest.get(2);


        List<Integer> listToiletIdPee = new ArrayList<>();
        for (int i = 0; i < numberOfUserPee; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));
            int index = (int)(Math.random() * listAllUsers.size());
            String message;
            if (listAllUsers.get(index).getAccountBalance() == 0 && listAllUsers.get(index).getAccountTurn() == 0) {
                message = listAllUsers.get(index).getFullName() + " đã hết số dư và số lươt.";
            } else {
                message = process(randomToiletId, listAllUsers.get(index).getAccountId(), "Đi vệ sinh (tiểu tiện)");
            }
            listUserCheckIn.add(message);
            listToiletIdPee.add(randomToiletId);
            listAllUsers.remove(index);
        }

        List<Integer> listToiletIdGuestPee = new ArrayList<>();
        for (int i = 0; i < numberOfGuestPee; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));
            String message = checkInGuest(randomToiletId, "Đi vệ sinh (tiểu tiện)");

            listUserCheckIn.add(message);
            listToiletIdPee.add(randomToiletId);
            listToiletIdGuestPee.add(randomToiletId);
        }

        List<Integer> listToiletIdPoop = new ArrayList<>();
        for (int i = 0; i < numberOfUserPoop; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));
            int index = (int)(Math.random() * listAllUsers.size());
            String message;
            if (listAllUsers.get(index).getAccountBalance() == 0 && listAllUsers.get(index).getAccountTurn() == 0) {
                message = listAllUsers.get(index).getFullName() + " đã hết số dư và số lươt.";
            } else if (toiletService.checkToilet(randomToiletId).equals("Not available")) {
                message = processFullToilet(randomToiletId, listAllUsers.get(index).getFullName());
            } else {
                try {
                    message = process(randomToiletId, listAllUsers.get(index).getAccountId(), "Đi vệ sinh (đại tiện)");
                } catch (BadRequestException e) {
                    message = processNotHave(randomToiletId, listAllUsers.get(index).getFullName(), "đại tiện");
                }

            }
            listUserCheckIn.add(message);
            listToiletIdPoop.add(randomToiletId);
            listAllUsers.remove(index);
        }

        List<Integer> listToiletIdGuestPoop = new ArrayList<>();
        for (int i = 0; i < numberOfGuestPoop; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));
            String message;
            try {
                message = checkInGuest(randomToiletId, "Đi vệ sinh (đại tiện)");
            } catch (BadRequestException e) {
                message = processNotHave(randomToiletId, "Khách vãng lai", "đại tiện");
            }
            if (toiletService.checkToilet(randomToiletId).equals("Not available")) {
                message = processFullToiletGuest(randomToiletId);
            }
            listUserCheckIn.add(message);
            listToiletIdPoop.add(randomToiletId);
            listToiletIdGuestPoop.add(randomToiletId);
        }

        List<Integer> listToiletIdTakeAShower = new ArrayList<>();
        for (int i = 0; i < numberOfUserTakeAShower; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));

            ToiletCapacityResponse toilet = toiletService.getCapacityOfToilet(randomToiletId);

            int index = (int)(Math.random() * listAllUsers.size());
            String message;
            if (toilet.getNumberOfBathroom() == 0) {
                message = processNoBathroom(randomToiletId, listAllUsers.get(index).getFullName());
            } else if (listAllUsers.get(index).getAccountBalance() == 0 && listAllUsers.get(index).getAccountTurn() == 0) {
                message = listAllUsers.get(index).getFullName() + " đã hết số dư và số lươt.";
            } else if (toiletService.checkToilet(randomToiletId).equals("Not available")) {
                message = processFullToilet(randomToiletId, listAllUsers.get(index).getFullName());
            } else {
                try {
                    message = process(randomToiletId, listAllUsers.get(index).getAccountId(), "Đi tắm");
                } catch (BadRequestException e) {
                    message = processNotHave(randomToiletId, listAllUsers.get(index).getFullName(), "đi tắm");
                }

            }
            listUserCheckIn.add(message);
            listToiletIdTakeAShower.add(randomToiletId);
            listAllUsers.remove(index);
        }

        List<Integer> listToiletIdGuestTakeAShower = new ArrayList<>();
        for (int i = 0; i < numberOfGuestTakeAShower; i++) {
            int randomToiletId = listToiletId.get(random.nextInt(listToiletId.size()));
            String message;
            ToiletCapacityResponse toilet = toiletService.getCapacityOfToilet(randomToiletId);
            if (toilet.getNumberOfBathroom() == 0) {
                message = processNoBathroomGuest(randomToiletId);
            } else if (toiletService.checkToilet(randomToiletId).equals("Not available")) {
                message = processFullToiletGuest(randomToiletId);
            } else {
                try {
                    message = checkInGuest(randomToiletId, "Đi tắm");
                } catch (BadRequestException e) {
                    message = processNotHave(randomToiletId, "Khách vãng lai", "đi tắm");
                }
            }

            listUserCheckIn.add(message);
            listToiletIdTakeAShower.add(randomToiletId);
            listToiletIdGuestTakeAShower.add(randomToiletId);
        }

        List<CheckInScriptTotal> listTotal = new ArrayList<>();

        Set<Integer> listDistinctToiletIdGuestPee = new HashSet<>(listToiletIdGuestPee);
        Map<Integer, Integer> mapToiletIdGuestPee = new HashMap<>();
        for (Integer toiletId: listDistinctToiletIdGuestPee) {
            mapToiletIdGuestPee.put(toiletId, Collections.frequency(listToiletIdGuestPee, toiletId));
        }

        Set<Integer> listDistinctToiletIdGuestPoop = new HashSet<>(listToiletIdGuestPoop);
        Map<Integer, Integer> mapToiletIdGuestPoop = new HashMap<>();
        for (Integer toiletId: listDistinctToiletIdGuestPoop) {
            mapToiletIdGuestPoop.put(toiletId, Collections.frequency(listToiletIdGuestPoop, toiletId));
        }

        Set<Integer> listDistinctToiletIdGuestTakeAShower = new HashSet<>(listToiletIdGuestTakeAShower);
        Map<Integer, Integer> mapToiletIdGuestTakeAShower = new HashMap<>();
        for (Integer toiletId: listDistinctToiletIdGuestTakeAShower) {
            mapToiletIdGuestTakeAShower.put(toiletId, Collections.frequency(listToiletIdGuestTakeAShower, toiletId));
        }

        Set<Integer> listDistinctToiletIdPee = new HashSet<>(listToiletIdPee);
        for (Integer toiletId: listDistinctToiletIdPee) {
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);

            CheckInScriptTotal checkInScriptTotal = new CheckInScriptTotal();
            checkInScriptTotal.setToiletId(toiletId);
            checkInScriptTotal.setToiletName(toiletEntity.get().getName());
            checkInScriptTotal.setPee(Collections.frequency(listToiletIdPee, toiletId));
            listTotal.add(checkInScriptTotal);
        }

        Set<Integer> listDistinctToiletIdPoop = new HashSet<>(listToiletIdPoop);
        for (Integer toiletId: listDistinctToiletIdPoop) {
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);

            AtomicBoolean check = new AtomicBoolean(false);
            listTotal.forEach(checkInScriptTotal -> {
                if (checkInScriptTotal.getToiletId() == toiletId) {
                    check.set(true);
                    checkInScriptTotal.setPoop(Collections.frequency(listToiletIdPoop, toiletId));
                }
            });

            if (!check.get()) {
                CheckInScriptTotal checkInScriptTotal = new CheckInScriptTotal();
                checkInScriptTotal.setToiletId(toiletEntity.get().getId());
                checkInScriptTotal.setToiletName(toiletEntity.get().getName());
                checkInScriptTotal.setPee(Collections.frequency(listToiletIdPoop, toiletId));
                listTotal.add(checkInScriptTotal);
            }
        }

        Set<Integer> listDistinctToiletIdTakeAShower = new HashSet<>(listToiletIdTakeAShower);
        for (Integer toiletId: listDistinctToiletIdTakeAShower) {
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);

            AtomicBoolean check = new AtomicBoolean(false);
            listTotal.forEach(checkInScriptTotal -> {
                if (checkInScriptTotal.getToiletId() == toiletId) {
                    check.set(true);
                    checkInScriptTotal.setBath(Collections.frequency(listToiletIdTakeAShower, toiletId));
                }
            });

            if (!check.get()) {
                CheckInScriptTotal checkInScriptTotal = new CheckInScriptTotal();
                checkInScriptTotal.setToiletId(toiletEntity.get().getId());
                checkInScriptTotal.setToiletName(toiletEntity.get().getName());
                checkInScriptTotal.setBath(Collections.frequency(listToiletIdTakeAShower, toiletId));
                listTotal.add(checkInScriptTotal);
            }
        }

        listTotal.forEach(checkInScriptTotal -> {
            int guestPee = 0;
            if (mapToiletIdGuestPee.get(checkInScriptTotal.getToiletId()) != null) {
                guestPee = mapToiletIdGuestPee.get(checkInScriptTotal.getToiletId());
            }

            int guestPoop = 0;
            if (mapToiletIdGuestPoop.get(checkInScriptTotal.getToiletId()) != null) {
                guestPoop = mapToiletIdGuestPoop.get(checkInScriptTotal.getToiletId());
            }

            int guestBath = 0;
            if (mapToiletIdGuestTakeAShower.get(checkInScriptTotal.getToiletId()) != null) {
                guestBath = mapToiletIdGuestTakeAShower.get(checkInScriptTotal.getToiletId());
            }

            checkInScriptTotal.setNumberOfGuest(guestPee + guestPoop + guestBath);
            checkInScriptTotal.setNumberOfUser(checkInScriptTotal.getPee()
                    + checkInScriptTotal.getPoop()
                    + checkInScriptTotal.getBath()
                    - checkInScriptTotal.getNumberOfGuest());
        });

        result.setListUserCheckIn(listUserCheckIn);
        result.setListTotal(listTotal);

        return result;
    }

    private String process(int toiletId, int accountId, String serviceName) {
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setToiletId(toiletId);
        checkInRequest.setAccountId(accountId);
        checkInRequest.setServiceName(serviceName);
        checkInRequest.setDatetime(initDate());
        CheckInResponse response = checkInService.userCheckIn(checkInRequest);
        return response.getFullName()
                + " đã check-in tại "
                + response.getToiletName()
                + " với "
                + response.getServiceName();
    }

    private String checkInGuest(int toiletId, String serviceName) {

        WalkInGuestCheckInRequest walkInGuestCheckInRequest = new WalkInGuestCheckInRequest();
        walkInGuestCheckInRequest.setToiletId(toiletId);
        walkInGuestCheckInRequest.setAccountId(toiletId);

        List<CheckInRequest> listCheckInRequest = new ArrayList<>();
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setToiletId(toiletId);
        checkInRequest.setAccountId(toiletId);
        checkInRequest.setServiceName(serviceName);
        checkInRequest.setQuantity(1);
        checkInRequest.setDatetime(initDate());
        listCheckInRequest.add(checkInRequest);

        walkInGuestCheckInRequest.setCheckInRequests(listCheckInRequest);

        List<CheckInResponse> response = checkInService.walkInGuestCheckIn(walkInGuestCheckInRequest);

        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);

        return "Khách vãng lai đã check-in tại "
                + toiletEntity.get().getName()
                + " với "
                + response.get(0).getServiceName();
    }

    private String initDate() {
        Date now = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(now);
    }

    private List<Integer> randomList(int m, int n) {
        List<Integer> result = new ArrayList<>();

        int arr[] = new int[m];

        for (int i = 0; i < n; i++) {
            arr[(int)(Math.random() * m)]++;
        }

        for (int i = 0; i < m; i++) {
            result.add(arr[i]);
        }

        return result;
    }

    private String processNoBathroom(int toiletId, String name) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        return name + " không thể đi tắm vì "
                + toiletEntity.get().getName()
                + " không có phòng tắm.";
    }

    private String processNoBathroomGuest(int toiletId) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        return "Khách vãng lai không thể đi tắm vì "
                + toiletEntity.get().getName()
                + " không có phòng tắm.";
    }

    private String processFullToilet(int toiletId, String name) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        return name + " không thể check-in vì "
                + toiletEntity.get().getName()
                + " đang đầy.";
    }

    private String processFullToiletGuest(int toiletId) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        return "Khách vãng lai không thể check-in vì "
                + toiletEntity.get().getName()
                + " đang đầy.";
    }

    private String processNotHave(int toiletId, String name, String service) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        return name + " không thể check-in vì "
                + toiletEntity.get().getName()
                + " hết phòng "
                + service
                + ".";
    }
}

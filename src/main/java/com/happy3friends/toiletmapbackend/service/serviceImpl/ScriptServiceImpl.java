package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.CheckInFullAToiletRequest;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import com.happy3friends.toiletmapbackend.service.ScriptService;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public List<String> checkInFullAToilet(CheckInFullAToiletRequest request) {
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

        List<UserInfoEntity> listAllUsers = userInfoRepository.findAll();

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
            } else {
                message = process(request.getToiletId(), listUsers.get(i).getAccountId(), "Đi tắm");
            }
            result.add(message);
        }
        for (int i = listUsers.size() - request.getNumberOfRestroom(); i < listUsers.size(); i++) {
            String message;
            if (i >= numberOfAvailableRestroom + listUsers.size() - request.getNumberOfRestroom()) {
                message = listUsers.get(i).getFullName() + " không thể đại tiện vì hết phòng";
            } else {
                message = process(request.getToiletId(), listUsers.get(i).getAccountId(), "Đi vệ sinh (đại tiện)");
            }

            result.add(message);
        }

        return result;
    }

    @Override
    public List<String> checkout(int toiletId) {
        Date date = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(date) + " 00:00:00";
        String endDate = dateFormat.format(date) + " 23:59:59";

        String now = DateTimeUtil.getTimestampNow().toString();

        List<Integer> list = checkInRepository.getListAvailableCheckIn(toiletId, startDate, endDate, now);

        List<String> result = new ArrayList<>();

        if (list.isEmpty()) {
            result.add("Nhà vệ sinh hiện tại không có người dùng nào để checkout");
            return result;
        }
        checkInRepository.checkout(toiletId, startDate, endDate, now);

        checkInRepository.getListUserByListCheckInId(list).forEach(u -> {
            result.add(u + " đã checkout");
        });

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

    private String initDate() {
        Date now = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(now);
    }
}

package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import com.happy3friends.toiletmapbackend.service.ScriptService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CheckInService checkInService;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(now);
    }
}

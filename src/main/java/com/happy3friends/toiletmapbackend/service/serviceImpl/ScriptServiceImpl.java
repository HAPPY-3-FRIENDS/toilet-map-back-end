package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
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

        Date now = DateTimeUtil.getDateNow();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(now);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < list100Users.size(); i++) {
            if (i < 13) {
                String message = process(4, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)", strDate);
                result.add(message);
            } else if (i < 16) {
                String message = process(5, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)", strDate);
                result.add(message);
            } else if (i < 26) {
                String message = process(45, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)", strDate);
                result.add(message);
            } else if (i < 56) {
                String message = process(232, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)", strDate);
                result.add(message);
            } else if (i < 64) {
                String message = process(233, list100Users.get(i).getAccountId(), "Đi vệ sinh (tiểu tiện)", strDate);
                result.add(message);
            } else if (i < 74) {
                String message = process(4, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)", strDate);
                result.add(message);
            } else if (i < 76) {
                String message = process(5, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)", strDate);
                result.add(message);
            } else if (i < 84) {
                String message = process(45, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)", strDate);
                result.add(message);
            } else if (i < 89) {
                String message = process(232, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)", strDate);
                result.add(message);
            } else if (i < 90) {
                String message = process(233, list100Users.get(i).getAccountId(), "Đi vệ sinh (đại tiện)", strDate);
                result.add(message);
            } else if (i < 92) {
                String message = process(4, list100Users.get(i).getAccountId(), "Đi tắm", strDate);
                result.add(message);
            } else if (i < 94) {
                String message = process(45, list100Users.get(i).getAccountId(), "Đi tắm", strDate);
                result.add(message);
            } else if (i < 99) {
                String message = process(232, list100Users.get(i).getAccountId(), "Đi tắm", strDate);
                result.add(message);
            } else if (i < 100) {
                String message = process(233, list100Users.get(i).getAccountId(), "Đi tắm", strDate);
                result.add(message);
            }
        }

        return result;
    }

    private String process(int toiletId, int accountId, String serviceName, String strDate) {
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setToiletId(toiletId);
        checkInRequest.setAccountId(accountId);
        checkInRequest.setServiceName(serviceName);
        checkInRequest.setDatetime(strDate);
        CheckInResponse response = checkInService.userCheckIn(checkInRequest);
        return response.getFullName()
                + " đã check-in tại "
                + response.getToiletName()
                + " với "
                + response.getServiceName();
    }
}

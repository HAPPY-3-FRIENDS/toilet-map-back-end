package com.happy3friends.toiletmapbackend.scheduler;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.response.StatisticForSuggestionResponse;
import com.happy3friends.toiletmapbackend.response.ToiletFacilityResponse;
import com.happy3friends.toiletmapbackend.service.StatisticService;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Scheduler {
    @Autowired
    private ToiletService toiletService;
    @Autowired
    private StatisticService statisticService;

    @Autowired
    private SuggestionService suggestionService;

    @Scheduled(cron = "0 0 0 1 1,5,9 ?")
//    @Scheduled(cron = "15 * * * * ?")
    public void scheduleTaskWithCronExpression() throws ParseException {
        Date endDate = DateTimeUtil.getDateNow();
//        String endDateStr = "01-01-2023";
//        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDateStr);
        Date startDate = DateUtils.addMonths(endDate, -4);

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

            double expectedCount = result.getHours() * (result.getNumberOfBathroom() * 2 + result.getNumberOfRestroom() * 3);

            if (result.getActualCount() >= expectedCount * 175 / 100) {
                String message = "Số lượt đi thực tế vượt 175% so với sức chứa, gợi ý mở thêm nhà vệ sinh gần đây hoặc mở thêm phòng vệ sinh.";

                SuggestionEntity entity = new SuggestionEntity();
                entity.setToiletId(toiletId);
                entity.setStartDate(new java.sql.Date(startDate.getTime()));
                entity.setEndDate(new java.sql.Date(endDate.getTime()));
                entity.setMessage(message);
                entity.setActualCount(result.getActualCount());
                entity.setExpectedCount(expectedCount);
                entity.setIsAccepted(false);
                suggestionService.save(entity);
            }
        });
    }
}

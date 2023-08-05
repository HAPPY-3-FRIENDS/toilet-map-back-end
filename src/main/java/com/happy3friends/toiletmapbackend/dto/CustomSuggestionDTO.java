package com.happy3friends.toiletmapbackend.dto;

import java.sql.Date;

public interface CustomSuggestionDTO {
    int getToiletId();
    String getName();
    String getAddress();
    String getWard();
    String getDistrict();
    String getProvince();
    String getMessage();
    Boolean getIsAccepted();
    Date getStartDate();
    Date getEndDate();
    Integer getActualCount();
    Double getExpectedCount();
    int getStreak();
    Boolean getIsLow();
}

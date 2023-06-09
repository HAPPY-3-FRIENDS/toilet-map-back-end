package com.happy3friends.toiletmapbackend.dto;

import java.sql.Time;
import java.util.Date;

public interface CustomToiletDetailsInfoDTO {
    Integer getId();
    String getToiletName();
    String getAddress();
    String getWard();
    String getDistrict();
    String getProvince();
    Double getLatitude();
    Double getLongitude();
    String getNearBy();
    Time getOpenTime();
    Time getCloseTime();
    Boolean getIsFree();
    Integer getMinPrice();
    Integer getMaxPrice();
    Integer getFacilityId();
    String getFacilityName();
    String getFacilityType();
    Integer getFacilityQuantity();
    String getFacilityDescription();
    Double getRatingStar();
    String getToiletImage();
    String getUsername();
    String getStatus();
    String getMessage();
    Boolean isAccepted();
    Date getStartDate();
    Date getEndDate();
    Integer getExpectedCount();
    Integer getActualCount();
}

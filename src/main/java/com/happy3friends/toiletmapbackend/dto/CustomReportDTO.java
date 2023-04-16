package com.happy3friends.toiletmapbackend.dto;

public interface CustomReportDTO {
    Integer getCompanyId();
    String getCompanyName();
    Integer getToiletId();
    String getToiletName();
    String getServiceName();
    Integer getTotalRevenue();
    Integer getWalkInGuestRevenue();
    Integer getWalkInGuestCount();
    Integer getUsingTurnRevenue();
    Integer getUsingTurnCount();
}

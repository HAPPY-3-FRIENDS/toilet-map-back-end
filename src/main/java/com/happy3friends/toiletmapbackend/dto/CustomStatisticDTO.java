package com.happy3friends.toiletmapbackend.dto;

public interface CustomStatisticDTO {
    Integer getCompanyId();
    String getCompanyName();
    Integer getToiletId();
    String getToiletName();
    String getServiceName();
    Integer getTotalRevenue();
    Integer getTotalTurn();
    Integer getWalkInGuestRevenue();
    Integer getWalkInGuestCount();
    Integer getUsingTurnRevenue();
    Integer getUsingTurnCount();
}

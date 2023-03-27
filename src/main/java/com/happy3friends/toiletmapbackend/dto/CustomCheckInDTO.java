package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomCheckInDTO {
    String getFullName();
    Date getDateTime();
    String getServiceName();
    String getPaymentMethod();
    Double getBalance();
    Integer getTurn();
    String getToiletName();
    int getToiletId();
}

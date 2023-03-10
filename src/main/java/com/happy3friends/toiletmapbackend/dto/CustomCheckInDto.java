package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomCheckInDto {
    String getFullName();
    Date getDateTime();
    String getServiceName();
    String getPaymentType();
    Double getBalance();
    Integer getTurn();
}

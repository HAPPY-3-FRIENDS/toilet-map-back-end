package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomCheckInDTO {
    public String getFullName();
    public Date getDateTime();
    public String getServiceName();
    public String getPaymentMethod();
    public Double getBalance();
    public Integer getTurn();
    public String getToiletName();
    public Integer getToiletId();
}

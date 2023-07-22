package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomPaymentDTO {
    int getId();
    int getTotal();
    String getMethod();
    Date getCreatedDate();
}

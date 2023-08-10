package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private int accountId;
    private int totalTurn;
    private int totalPrice;
    private String paymentMethod;
    @JsonFormat(pattern = DateTimeConstant.dd_MM_yyyy__HH_mm_ss, timezone = DateTimeConstant.TIME_ZONE)
    private Date dateTime;
}

package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInResponse {
    private String fullName;
    @JsonFormat(pattern = DateTimeConstant.dd_MM_yyyy__HH_mm_ss)
    private Date dateTime;
    private String serviceName;
    private String paymentMethod;
    private Integer balance;
    private Integer turn;
    private String toiletName;
    private int toiletId;
}

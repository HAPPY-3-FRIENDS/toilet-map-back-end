package com.happy3friends.toiletmapbackend.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletCreateRequest {
    private int companyId;
    private String username;
    private String password;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private double latitude;
    private double longitude;
    private boolean isFree;
    @JsonFormat(pattern = DateTimeConstant.HH_mm)
    private Timestamp openTime;
    @JsonFormat(pattern = DateTimeConstant.HH_mm)
    private Timestamp closeTime;
    private String status;
    private List<String> toiletImages;
    private List<ToiletFacilityDTO> toiletFacilities;
}

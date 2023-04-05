package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletResponse {
    private int id;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private double latitude;
    private double longitude;
    private String nearBy;
    private boolean isFree;
    private Time openTime;
    private Time closeTime;
    private String status;
}

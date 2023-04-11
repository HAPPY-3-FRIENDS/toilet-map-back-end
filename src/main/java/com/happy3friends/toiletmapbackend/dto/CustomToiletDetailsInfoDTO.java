package com.happy3friends.toiletmapbackend.dto;

import java.sql.Time;

public interface CustomToiletDetailsInfoDTO {
    public Integer getId();
    public String getToiletName();
    public String getAddress();
    public String getWard();
    public String getDistrict();
    public String getProvince();
    public Double getLatitude();
    public Double getLongitude();
    public String getNearBy();
    public Time getOpenTime();
    public Time getCloseTime();
    public Boolean getIsFree();
    public Integer getMinPrice();
    public Integer getMaxPrice();
    public String getFacilityName();
    public String getFacilityType();
    public Integer getFacilityQuantity();
    public String getFacilityDescription();
    public Double getRatingStar();
    public String getToiletImage();
    public String getUsername();
    public String getStatus();
}

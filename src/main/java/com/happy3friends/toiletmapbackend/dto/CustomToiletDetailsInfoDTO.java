package com.happy3friends.toiletmapbackend.dto;

import java.sql.Time;

public interface CustomToiletDetailsInfoDTO {
    public int getId();
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
    public boolean getIsFree();
    public int getMinPrice();
    public int getMaxPrice();
    public String getFacilityName();
    public int getFacilityQuantity();
    public String getFacilityDescription();
    public double getRatingStar();
    public String getToiletImage();
}

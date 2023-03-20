package com.happy3friends.toiletmapbackend.dto;

public interface CustomAccountInfoDTO {
    int getId();
    String getUsername();
    String getPassword();
    String getFullName();
    String getRole();
    String getDefaultPayment();
    int getAccountBalance();
}

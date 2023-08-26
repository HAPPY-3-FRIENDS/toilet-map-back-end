package com.happy3friends.toiletmapbackend.dto;

public interface CustomAccountInfoDTO {
    int getAccountId();
    String getUsername();
    String getPassword();
    String getFullName();
    String getAvatar();
    String getRole();
    String getDefaultPayment();
    int getAccountBalance();
    int getAccountTurn();
    String getStatus();
}

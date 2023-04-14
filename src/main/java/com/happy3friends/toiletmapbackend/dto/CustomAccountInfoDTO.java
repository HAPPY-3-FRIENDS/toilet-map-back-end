package com.happy3friends.toiletmapbackend.dto;

public interface CustomAccountInfoDTO {
    public int getAccountId();
    public String getUsername();
    public String getPassword();
    public String getFullName();
    public String getGmail();
    public String getAvatar();
    public String getRole();
    public String getDefaultPayment();
    public int getAccountBalance();
    public int getAccountTurn();
}

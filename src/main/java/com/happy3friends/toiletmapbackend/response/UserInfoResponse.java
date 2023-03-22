package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private int accountId;
    private String fullName;
    private String gmail;
    private String avatar;
    private String defaultPayment;
}

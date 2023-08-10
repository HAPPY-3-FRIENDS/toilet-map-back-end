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
    private Integer accountId;
    private String fullName;
    private String avatar;
    private String defaultPayment;
    private Integer accountBalance;
    private Integer accountTurn;
    private String username;
}

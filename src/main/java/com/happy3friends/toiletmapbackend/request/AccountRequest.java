package com.happy3friends.toiletmapbackend.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountRequest {
    @NotBlank
    private String username;
    private String password;
    private String fullName;
    private String roleName;
    private int companyId;
}

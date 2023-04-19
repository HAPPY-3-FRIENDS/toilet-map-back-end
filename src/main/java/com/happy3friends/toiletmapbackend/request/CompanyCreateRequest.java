package com.happy3friends.toiletmapbackend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyCreateRequest {
    @NotNull
    private String name;
    @NotNull
    private String logo;
    @NotNull
    private String address;
    @NotNull
    private String ward;
    @NotNull
    private String district;
    @NotNull
    private String province;
    @NotNull
    private String phone;
    @NotNull
    private String username;
    @NotNull
    private String password;
}

package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyHasStatusResponse {
    private int id;
    private String name;
    private String logo;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String phone;
    private String status;
    private int numberOfReport;
}

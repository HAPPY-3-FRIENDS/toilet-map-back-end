package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateToiletInfoResponse {
    private Integer id;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private List<String> toiletImagesById;
}

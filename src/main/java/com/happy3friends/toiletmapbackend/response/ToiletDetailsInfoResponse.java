package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import lombok.*;

import java.sql.Time;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ToiletDetailsInfoResponse {
    private int id;
    private String toiletName;
    private String address;
    private String ward;
    private String district;
    private String province;
    private Double latitude;
    private Double longitude;
    private String nearBy;
    private Time openTime;
    private Time closeTime;
    private boolean isFree;
    private int minPrice;
    private int maxPrice;
    private List<ToiletFacilityDTO> toiletFacilityDTOS;
    private List<String> toiletImageSources;
    private double ratingStar;
}

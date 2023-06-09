package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import lombok.*;

import java.util.Date;
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
    private double latitude;
    private double longitude;
    private String nearBy;
    private String openTime;
    private String closeTime;
    private boolean isFree;
    private int minPrice;
    private int maxPrice;
    private List<ToiletFacilityDTO> toiletFacilities;
    private List<String> toiletImageSources;
    private double ratingStar;
    private String username;
    private String status;
    private String duration;
    private String distance;
    private String message;
    private Boolean isAccepted;
    private Date startDate;
    private Date endDate;
    private Integer expectedCount;
    private Integer actualCount;
}

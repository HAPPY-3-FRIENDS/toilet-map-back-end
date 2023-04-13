package com.happy3friends.toiletmapbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletFacilityDTO {
    private int facilityId;
    private String facilityName;
    private String facilityType;
    private int quantity;
    private String description;
}

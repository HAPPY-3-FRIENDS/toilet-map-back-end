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
    private String facilityName;
    private int quantity;
    private String description;
}

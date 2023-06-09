package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletFacilityResponse {
    private int id;
    private int toiletId;
    private int facilityId;
    private Integer quantity;
    private String description;
}

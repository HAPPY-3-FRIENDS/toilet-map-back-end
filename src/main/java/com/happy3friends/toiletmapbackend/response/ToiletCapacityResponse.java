package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletCapacityResponse {
    private int id;
    private String name;
    private int numberOfRestroom;
    private int maxNumberOfRestroom;
    private int numberOfBathroom;
    private int maxNumberOfBathroom;
    private int numberOfDisabledRestroom;
    private int maxNumberOfDisabledRestroom;

}

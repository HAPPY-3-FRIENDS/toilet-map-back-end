package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatisticForSuggestionResponse {
    private Integer toiletId;
    private Integer actualCount;
    private float hours;
    private int numberOfRestroom;
    private int numberOfBathroom;
}

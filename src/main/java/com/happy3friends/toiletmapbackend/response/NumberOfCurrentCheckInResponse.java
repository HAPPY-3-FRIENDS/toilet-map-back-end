package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NumberOfCurrentCheckInResponse {
    private int numNotAvailableRestroom;
    private int numberOfRestroom;
    private int numNotAvailableBathroom;
    private int numberOfBathroom;
    private int waitingRestroomTime;
    private int waitingBathroomTime;
}

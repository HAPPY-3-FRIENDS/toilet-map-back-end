package com.happy3friends.toiletmapbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckInFullAToiletTotal {
    private int checkInSuccess;
    private int checkInFail;
    private int toiletRoomEmpty;
    private int bathRoomEmpty;
}

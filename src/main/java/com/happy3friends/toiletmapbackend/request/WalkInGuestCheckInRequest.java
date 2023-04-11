package com.happy3friends.toiletmapbackend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalkInGuestCheckInRequest {
    private int toiletId;
    private int accountId;
    List<CheckInRequest> checkInRequests;
}

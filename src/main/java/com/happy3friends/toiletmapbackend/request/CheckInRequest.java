package com.happy3friends.toiletmapbackend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {
    private int toiletId;
    private String serviceName;
    private String datetime;
}

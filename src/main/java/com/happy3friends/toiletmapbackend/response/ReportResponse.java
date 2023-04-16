package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportResponse {
    private Integer companyId;
    private String companyName;
    private Integer toiletId;
    private String toiletName;
    private String serviceName;
    private Integer totalRevenue;
    private Integer walkInGuestRevenue;
    private Integer walkInGuestCount;
    private Integer usingTurnRevenue;
    private Integer usingTurnCount;
}

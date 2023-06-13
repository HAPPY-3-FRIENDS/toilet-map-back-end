package com.happy3friends.toiletmapbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SuggestionDTO {
    private int id;
    private int toiletId;
    private String message;
    private Boolean isAccepted;
    private Date startDate;
    private Date endDate;
    private Integer actualCount;
    private Double expectedCount;
}

package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnnouncementResponse {
    private int id;
    private String title;
    private String url;
    private String imageSource;
    @JsonFormat(pattern="dd-MM-yyyy", timezone = DateTimeConstant.TIME_ZONE)
    private Date startDate;
    @JsonFormat(pattern="dd-MM-yyyy", timezone = DateTimeConstant.TIME_ZONE)
    private Date endDate;
    private String description;
    private String type;
}

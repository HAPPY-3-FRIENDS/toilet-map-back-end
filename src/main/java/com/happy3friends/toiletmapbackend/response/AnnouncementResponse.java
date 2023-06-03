package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnnouncementResponse {
    private int id;
    private String title;
    private String url;
    private String imageSource;
    private Date startDate;
    private Date endDate;
    private String description;
    private String type;
}

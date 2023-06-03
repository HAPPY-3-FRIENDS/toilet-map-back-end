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
public class CreateAnnouncementRequest {
    private String title;
    private String url;
    private String imageSource;
    private Date startDate;
    private Date endDate;
    private String description;
    private String type;
}

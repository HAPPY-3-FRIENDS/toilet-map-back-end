package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date endDate;
    private String description;
    private String type;
}

package com.happy3friends.toiletmapbackend.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date endDate;
    private String description;
    private String type;
}

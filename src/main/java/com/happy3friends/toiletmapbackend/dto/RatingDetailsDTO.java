package com.happy3friends.toiletmapbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatingDetailsDTO {
    private int id;
    private String fullName;
    private Integer star;
    private String comment;
    private Date dateTime;
    private List<String> imageSources;
    private String avatar;
    private String status;
    private List<String> commonComments;
}

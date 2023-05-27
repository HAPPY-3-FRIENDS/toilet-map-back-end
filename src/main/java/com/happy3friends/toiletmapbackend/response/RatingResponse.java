package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
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
public class RatingResponse {
    private int id;
    private String fullName;
    private int star;
    private String comment;
    @JsonFormat(pattern = DateTimeConstant.dd_MM_yyyy__HH_mm_ss)
    private Date dateTime;
    private List<String> imageSources;
    private String avatar;
    private String status;
    private List<String> commonComments;
}

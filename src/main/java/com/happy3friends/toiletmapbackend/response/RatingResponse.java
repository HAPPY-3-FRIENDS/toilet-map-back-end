package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.dto.RatingImageDTO;

import java.util.Date;
import java.util.List;

public class RatingResponse {
    private int star;
    private String comment;
    @JsonFormat(pattern = DateTimeConstant.dd_MM_yyyy__HH_mm_ss)
    private Date dateTime;
    private List<RatingImageDTO> listRatingImage;
}

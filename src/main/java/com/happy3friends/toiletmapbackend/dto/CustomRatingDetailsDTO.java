package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomRatingDetailsDTO {
    Integer getId();
    String getFullName();
    Integer getStar();
    String getComment();
    Date getDateTime();
    String getImageSource();
    String getAvatar();
    String getStatus();
    String getCommonComment();
}

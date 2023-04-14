package com.happy3friends.toiletmapbackend.dto;

import java.util.Date;

public interface CustomRatingDetailsDTO {
    public Integer getId();
    public String getFullName();
    public Integer getStar();
    public String getComment();
    public Date getDateTime();
    public String getImageSource();
}

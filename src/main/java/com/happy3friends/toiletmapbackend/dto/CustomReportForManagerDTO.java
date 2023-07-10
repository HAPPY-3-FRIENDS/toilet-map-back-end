package com.happy3friends.toiletmapbackend.dto;

import java.sql.Timestamp;

public interface CustomReportForManagerDTO {
    Integer getId();
    String getName();
    String getMessage();
    String getStatus();
    Timestamp getCreateDate();
}

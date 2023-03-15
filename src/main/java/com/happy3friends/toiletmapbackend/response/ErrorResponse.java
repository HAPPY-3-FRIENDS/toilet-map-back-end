package com.happy3friends.toiletmapbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    @JsonFormat(pattern = DateTimeConstant.dd_MM_yyyy__HH_mm_ss)
    private ZonedDateTime timestamp;
    private int status;
    private String message;
    private List<String> errors;
}

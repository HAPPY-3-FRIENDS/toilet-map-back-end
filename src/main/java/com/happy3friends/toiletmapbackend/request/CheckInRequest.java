package com.happy3friends.toiletmapbackend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {
    private int accountId;
    @NotBlank
    private String serviceName;
    @NotBlank
    private String datetime;
}

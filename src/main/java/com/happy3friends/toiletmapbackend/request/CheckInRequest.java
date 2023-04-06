package com.happy3friends.toiletmapbackend.request;

import com.happy3friends.toiletmapbackend.annotation.ServiceAnnotation;
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
    @ServiceAnnotation
    private String serviceName;
    private int quantity;
    private String datetime;
}

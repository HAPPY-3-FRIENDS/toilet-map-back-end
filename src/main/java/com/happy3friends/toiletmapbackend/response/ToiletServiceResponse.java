package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.dto.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToiletServiceResponse {
    private int id;
    private int toiletId;
    private ServiceDTO service;
}

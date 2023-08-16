package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.dto.ServiceDTO;
import com.happy3friends.toiletmapbackend.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllService();
    void updateBatchServices(List<ServiceDTO> serviceDTOS);

    Integer getTurnPrice();

    void updateTurnPrice(int turnPrice);
}

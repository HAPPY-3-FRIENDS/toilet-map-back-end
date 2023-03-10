package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;

import java.util.List;

public interface ToiletService {
    List<CustomCheckInDTO> toiletCheckInHistoriesByToiletId(int toiletId);
}

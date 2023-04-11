package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.ToiletServiceResponse;

import java.util.List;

public interface ToiletServiceService {
    List<ToiletServiceResponse> getToiletServicesByToiletId(int toiletId);
}

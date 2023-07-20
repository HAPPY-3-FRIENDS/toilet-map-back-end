package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CheckInFullAToiletRequest;

import java.util.List;

public interface ScriptService {
    List<String> random100UserCheckIn();

    List<String> checkInFullAToilet(CheckInFullAToiletRequest request);

    List<String> checkout(int toiletId);
}

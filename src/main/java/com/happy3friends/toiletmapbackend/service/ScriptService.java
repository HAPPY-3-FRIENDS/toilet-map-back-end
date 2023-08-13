package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CheckInFullAToiletRequest;
import com.happy3friends.toiletmapbackend.request.CheckInScriptRequest;
import com.happy3friends.toiletmapbackend.response.CheckInFullAToiletResponse;
import com.happy3friends.toiletmapbackend.response.CheckInScriptResponse;
import com.happy3friends.toiletmapbackend.response.CheckoutResponse;
import com.happy3friends.toiletmapbackend.response.SuggestionSchedulerResponse;

import java.text.ParseException;
import java.util.List;

public interface ScriptService {
    List<String> random100UserCheckIn();

    CheckInFullAToiletResponse checkInFullAToilet(CheckInFullAToiletRequest request);

    CheckoutResponse checkout(int toiletId);

    List<SuggestionSchedulerResponse> runScheduler(String date) throws ParseException;
    CheckInScriptResponse randomUserCheckIn(CheckInScriptRequest request);
}

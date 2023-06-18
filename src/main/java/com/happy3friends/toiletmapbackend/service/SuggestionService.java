package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.response.SuggestionAdminResponse;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface SuggestionService {
    void save(SuggestionEntity entity);

    void updateAcceptedSuggestion(List<Integer> suggestionIds, Boolean isAccepted);

    List<SuggestionAdminResponse> getListOfSuggestions() throws ParseException;

    SuggestionEntity getPreviousQuarterSuggestion(int toiletId, Date endDate);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;

import java.util.List;

public interface SuggestionService {
    void save(SuggestionEntity entity);

    void updateAcceptedSuggestion(List<Integer> suggestionIds, Boolean isAccepted);
}

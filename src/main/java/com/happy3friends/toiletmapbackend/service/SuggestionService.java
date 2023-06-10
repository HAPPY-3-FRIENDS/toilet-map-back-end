package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.response.SuggestionResponse;

import java.util.Map;

public interface SuggestionService {
    void save(SuggestionEntity entity);

    SuggestionResponse updateSuggestion(Integer id, Map<String, Object> fields);
}

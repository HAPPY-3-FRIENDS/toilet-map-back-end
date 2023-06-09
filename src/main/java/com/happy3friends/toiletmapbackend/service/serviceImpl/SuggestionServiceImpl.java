package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.repository.SuggestionRepository;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionServiceImpl implements SuggestionService {
    @Autowired
    private SuggestionRepository suggestionRepository;

    @Override
    public void save(SuggestionEntity entity) {
        suggestionRepository.save(entity);
    }
}

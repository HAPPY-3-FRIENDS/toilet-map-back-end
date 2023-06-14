package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.repository.SuggestionRepository;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionServiceImpl.class);

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private SuggestionMapper suggestionMapper;

    @Override
    public void save(SuggestionEntity entity) {
        suggestionRepository.save(entity);
    }

    @Override
    public void updateAcceptedSuggestion(List<Integer> suggestionIds, Boolean isAccepted) {
        List<SuggestionEntity> suggestionEntity = suggestionRepository.findAllById(suggestionIds);
        if (suggestionEntity.size() != suggestionIds.size())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION, ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION.getMessage());

        LOGGER.info("-- Update Suggestion - Start update isAccepted field for suggestion! --");
        suggestionEntity.forEach(entity -> entity.setIsAccepted(isAccepted));
        suggestionRepository.saveAll(suggestionEntity);
        LOGGER.info("-- Update Suggestion - Finish update isAccepted field for suggestion! --");
    }
}

package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.repository.SuggestionRepository;
import com.happy3friends.toiletmapbackend.response.SuggestionResponse;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

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
    public SuggestionResponse updateSuggestion(Integer id, Map<String, Object> fields) {
        Optional<SuggestionEntity> suggestionEntity = suggestionRepository.findById(id);
        if (!suggestionEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION, ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION.getMessage());

        LOGGER.info("-- Update Suggestion - Start save Suggestion Entity and its information! --");
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(SuggestionEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, suggestionEntity.get(), value);
        });

        SuggestionEntity entity = suggestionRepository.save(suggestionEntity.get());
        LOGGER.info("-- Update Suggestion - Finish save Suggestion Entity and its information! --");
        return suggestionMapper.convertSuggestionEntityToSuggestionResponse(entity);
    }
}

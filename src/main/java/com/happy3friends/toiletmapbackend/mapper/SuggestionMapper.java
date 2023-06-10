package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.response.SuggestionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SuggestionMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SuggestionResponse convertSuggestionEntityToSuggestionResponse(SuggestionEntity suggestionEntity) {
        return Objects.isNull(suggestionEntity)
                ? null
                : modelMapper.map(suggestionEntity, SuggestionResponse.class);
    }
}

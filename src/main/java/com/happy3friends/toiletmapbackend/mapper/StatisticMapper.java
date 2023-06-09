package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomStatisticDTO;
import com.happy3friends.toiletmapbackend.dto.CustomStatisticForSuggestionDTO;
import com.happy3friends.toiletmapbackend.response.StatisticForSuggestionResponse;
import com.happy3friends.toiletmapbackend.response.StatisticResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StatisticMapper {

    @Autowired
    private ModelMapper modelMapper;

    public StatisticResponse convertCustomStatisticDTOToStatisticResponse(CustomStatisticDTO customStatisticDTO) {
        return Objects.isNull(customStatisticDTO)
                ? null
                : modelMapper.map(customStatisticDTO, StatisticResponse.class);
    }

    public StatisticForSuggestionResponse convertCustomStatisticForSuggestionDTOToStatisticForSuggestionResponse(CustomStatisticForSuggestionDTO customStatisticForSuggestionDTO) {
        return Objects.isNull(customStatisticForSuggestionDTO)
                ? null
                : modelMapper.map(customStatisticForSuggestionDTO, StatisticForSuggestionResponse.class);
    }
}

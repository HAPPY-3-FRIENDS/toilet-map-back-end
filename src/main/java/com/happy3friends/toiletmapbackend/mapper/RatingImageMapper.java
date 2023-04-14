package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.RatingImageDTO;
import com.happy3friends.toiletmapbackend.entity.RatingImageEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RatingImageMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingImageMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public RatingImageEntity convertRatingImageDTOToRatingImageEntity(RatingImageDTO ratingImageDTO) {
        return Objects.isNull(ratingImageDTO)
                ? null
                : modelMapper.map(ratingImageDTO, RatingImageEntity.class);
    }

    public RatingImageDTO convertRatingImageEntityToRatingImageDTO(RatingImageEntity ratingImageEntity) {
        return Objects.isNull(ratingImageEntity)
                ? null
                : modelMapper.map(ratingImageEntity, RatingImageDTO.class);
    }
}

package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.dto.RatingDetailsDTO;
import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RatingMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public RatingEntity convertRatingRequestToRatingEntity(RatingRequest ratingRequest) {
        return Objects.isNull(ratingRequest)
                ? null
                : modelMapper.map(ratingRequest, RatingEntity.class);
    }

    public RatingResponse convertRatingEntityToRatingResponse(RatingEntity ratingEntity) {
        return Objects.isNull(ratingEntity)
                ? null
                : modelMapper.map(ratingEntity, RatingResponse.class);
    }

    public RatingDetailsDTO convertCustomRatingDetailsDTOToRatingDetailsDTO(CustomRatingDetailsDTO customRatingDetailsDTO) {
        return Objects.isNull(customRatingDetailsDTO)
                ? null
                : modelMapper.map(customRatingDetailsDTO, RatingDetailsDTO.class);
    }

    public RatingResponse convertRatingDetailsDTOToRatingResponse(RatingDetailsDTO ratingDetailsDTO) {
        return Objects.isNull(ratingDetailsDTO)
                ? null
                : modelMapper.map(ratingDetailsDTO, RatingResponse.class);
    }
}

package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
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
}

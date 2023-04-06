package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import com.happy3friends.toiletmapbackend.mapper.RatingMapper;
import com.happy3friends.toiletmapbackend.repository.RatingImageRepository;
import com.happy3friends.toiletmapbackend.repository.RatingRepository;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import com.happy3friends.toiletmapbackend.service.RatingService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingImageRepository ratingImageRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public RatingResponse createRating(int toiletId, RatingRequest ratingRequest) {
        RatingEntity ratingEntity = ratingMapper.convertRatingRequestToRatingEntity(ratingRequest);
        ratingEntity.getRatingImagesById().forEach(dto -> dto.setRatingByRatingId(ratingEntity));
        ratingEntity.setToiletId(toiletId);
        ratingEntity.setDateTime(DateTimeUtil.getTimestampNow());
        ratingRepository.save(ratingEntity);
        return null;
    }
}

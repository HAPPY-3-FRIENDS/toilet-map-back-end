package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;

import java.util.List;
import java.util.Map;

public interface RatingService {
    List<RatingResponse> getAllRatings(Integer toiletId, BasePaginationRequest paginationRequest);

    RatingResponse createRating(RatingRequest ratingRequest);

    int count(Integer toiletId);

    RatingResponse update(int id, Map<String, Object> fields);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;

public interface RatingService {
    RatingResponse createRating(int toiletId, RatingRequest ratingRequest);
}

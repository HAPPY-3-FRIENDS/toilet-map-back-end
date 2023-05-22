package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;

import java.util.List;

public interface SensitiveWordService {
    List<SensitiveWordEntity> getAllSensitiveWords(BasePaginationRequest paginationRequest);

    int count();

    void create(String word);
}

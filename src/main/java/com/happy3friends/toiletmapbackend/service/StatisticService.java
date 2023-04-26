package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.response.StatisticResponse;

import java.util.List;

public interface StatisticService {
    List<StatisticResponse> getAllStatistics(
            Integer companyId,
            Integer toiletId,
            String fromStrDate,
            String toStrDate,
            BasePaginationRequest paginationRequest);
    StatisticResponse getTotalStatisticOfMonth(Integer companyId, Integer toiletId);

    int count(Integer companyId, Integer toiletId, String fromStrDate, String toStrDate);
}

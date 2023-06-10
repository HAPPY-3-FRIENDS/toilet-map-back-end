package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.response.ReportResponse;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getReports(BasePaginationRequest paginationRequest);
}

package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.CreateReportRequest;
import com.happy3friends.toiletmapbackend.request.UpdateListReportRequest;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponseForManager;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getReports(BasePaginationRequest paginationRequest);

    CreateReportResponse createReport(CreateReportRequest request);

    CreateReportResponse updateStatus(int id, String message);

    List<ReportResponseForManager> getReportsForManager(int companyId ,BasePaginationRequest paginationRequest);

    int countReportsForManager(int id);

    List<CreateReportResponse> updateListReports(UpdateListReportRequest request);
}

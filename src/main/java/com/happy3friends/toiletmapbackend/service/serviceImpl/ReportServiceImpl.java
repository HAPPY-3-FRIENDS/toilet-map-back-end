package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.ReportStatusConstant;
import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ReportMapper;
import com.happy3friends.toiletmapbackend.repository.ReportRepository;
import com.happy3friends.toiletmapbackend.request.CreateReportRequest;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import com.happy3friends.toiletmapbackend.service.ReportService;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<ReportResponse> getReports(BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.TOILET_ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CustomReportDTO> result = reportRepository.getReports(pageable);
        return null;
    }

    @Override
    public CreateReportResponse createReport(CreateReportRequest request) {
        ReportEntity entity = new ReportEntity();
        entity.setToiletId(request.getToiletId());
        entity.setMessage(request.getMessage());
        entity.setStatus(ReportStatusConstant.NEW);

        return reportMapper.convertReportEntitytoCreateReportResponse(reportRepository.save(entity));
    }

    @Override
    public CreateReportResponse updateStatus(int id, String message) {

        Optional<ReportEntity> entity = reportRepository.findById(id);
        if (!entity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_REPORT, ToiletMapErrorCodeEnum.NOT_FOUND_REPORT.getMessage());

        ReportEntity result = entity.get();
        result.setStatus(message);

        return reportMapper.convertReportEntitytoCreateReportResponse(reportRepository.save(result));
    }
}

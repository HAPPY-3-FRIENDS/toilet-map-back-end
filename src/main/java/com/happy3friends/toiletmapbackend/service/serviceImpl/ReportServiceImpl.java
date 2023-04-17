package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ReportMapper;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.ReportRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import com.happy3friends.toiletmapbackend.service.ReportService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckInServiceImpl.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ReportMapper reportMapper;

    private List<ReportResponse> getAllReportsByCompanyId(
            Integer companyId,
            Date fromDate,
            Date toDate,
            BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort - Default sort by highest revenue
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.TOTAL_REVENUE);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Company ID
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException("Company", "Id", companyId);

        List<CustomReportDTO> customReportDTOS = reportRepository.getAllReportsByCompanyId(companyId, fromDate, toDate, pageable);

        return customReportDTOS.stream()
                .map(dto -> reportMapper.convertCustomReportDTOToReportResponse(dto))
                .collect(Collectors.toList());
    }

    private List<ReportResponse> getAllReportsByToiletId(
            Integer toiletId,
            Date fromDate,
            Date toDate) {

        // Validate Toilet ID
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        List<CustomReportDTO> customReportDTOS = reportRepository.getAllReportsByToiletId(toiletId, fromDate, toDate);

        return customReportDTOS.stream()
                .map(dto -> reportMapper.convertCustomReportDTOToReportResponse(dto))
                .collect(Collectors.toList());
    }

    private List<ReportResponse> getAllReports(Date fromDate, Date toDate, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.TOTAL_REVENUE);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CustomReportDTO> customReportDTOS = reportRepository.getAllReports(fromDate, toDate, pageable);

        return customReportDTOS.stream()
                .map(dto -> reportMapper.convertCustomReportDTOToReportResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReports(
            Integer companyId,
            Integer toiletId,
            String fromStrDate,
            String toStrDate,
            BasePaginationRequest paginationRequest) {

        Date fromDate = null;
        Date toDate = null;

        // If fromDate && toDate is null -> Default fromDate and toDate is currentMonth
        if (fromStrDate == null || toStrDate == null) {
            toDate = DateTimeUtil.getDateNow();
            fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
        } else {  // Validate fromDate & toDate
            fromDate = DateTimeUtil.convertStringToDate(fromStrDate, DateTimeConstant.dd_MM_yyyy);
            toDate = DateTimeUtil.convertStringToDate(toStrDate, DateTimeConstant.dd_MM_yyyy);
            if (fromDate != null && fromDate.after(toDate))
                throw new BadRequestException("Invalid Date!");
        }

        if (companyId != null && toiletId == null) {
            return getAllReportsByCompanyId(companyId, fromDate, toDate, paginationRequest);
        } else if (companyId == null && toiletId != null) {
            return getAllReportsByToiletId(toiletId, fromDate, toDate);
        } else { // List of Total revenue of each companies in System
            return getAllReports(fromDate, toDate, paginationRequest);
        }
    }

    private ReportResponse getTotalReportOfMonthByCompanyId(Integer companyId, Date fromDate, Date toDate) {
        // Validate Company ID
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException("Company", "Id", companyId);

        CustomReportDTO customReportDTO = reportRepository.getTotalReportOfMonthByCompanyId(companyId, fromDate, toDate);

        return reportMapper.convertCustomReportDTOToReportResponse(customReportDTO);
    }

    private ReportResponse getTotalReportOfMonthByToiletId(Integer toiletId, Date fromDate, Date toDate) {
        // Validate Toilet ID
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        CustomReportDTO customReportDTO = reportRepository.getTotalReportOfMonthByToiletId(toiletId, fromDate, toDate);

        return reportMapper.convertCustomReportDTOToReportResponse(customReportDTO);
    }

    @Override
    public ReportResponse getTotalReportOfMonth(Integer companyId, Integer toiletId) {
        Date toDate = DateTimeUtil.getDateNow();
        Date fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();

        LOGGER.info("ZonedDateTime: " + ZonedDateTime.now(ZoneId.of(DateTimeConstant.ZONE_ID)));
        LOGGER.info("OffsetDateTime: " + OffsetDateTime.now(ZoneOffset.of("+07:00")));
        LOGGER.info("getZoneDateTimeNow: " + DateTimeUtil.getZoneDateTimeNow());
        LOGGER.info("getDateNow: " + toDate + " getFirstDateOfCurrentMonth: " + fromDate);
        LOGGER.info("convertZoneDateTimeToDate123: " + DateTimeUtil.getZoneDateTimeNow().toInstant());
        LOGGER.info("convertZoneDateTimeToDate: " + DateTimeUtil.convertZoneDateTimeToDate(DateTimeUtil.getZoneDateTimeNow()));

        if (companyId != null && toiletId == null) {
            return getTotalReportOfMonthByCompanyId(companyId, fromDate, toDate);
        } else if (companyId == null && toiletId != null) {
            return getTotalReportOfMonthByToiletId(toiletId, fromDate, toDate);
        } else {
            CustomReportDTO customReportDTO = reportRepository.getTotalReportOfMonth(fromDate, toDate);
            return reportMapper.convertCustomReportDTOToReportResponse(customReportDTO);
        }
    }
}

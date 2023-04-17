package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomStatisticDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.StatisticMapper;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.StatisticRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.StatisticResponse;
import com.happy3friends.toiletmapbackend.service.StatisticService;
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
public class StatisticServiceImpl implements StatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StatisticMapper statisticMapper;

    private List<StatisticResponse> getAllStatisticsByCompanyId(
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
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        List<CustomStatisticDTO> customStatisticDTOS = statisticRepository.getAllStatisticsByCompanyId(companyId, fromDate, toDate, pageable);

        return customStatisticDTOS.stream()
                .map(dto -> statisticMapper.convertCustomStatisticDTOToStatisticResponse(dto))
                .collect(Collectors.toList());
    }

    private List<StatisticResponse> getAllStatisticsByToiletId(
            Integer toiletId,
            Date fromDate,
            Date toDate) {

        // Validate Toilet ID
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        List<CustomStatisticDTO> customStatisticDTOS = statisticRepository.getAllStatisticsByToiletId(toiletId, fromDate, toDate);

        return customStatisticDTOS.stream()
                .map(dto -> statisticMapper.convertCustomStatisticDTOToStatisticResponse(dto))
                .collect(Collectors.toList());
    }

    private List<StatisticResponse> getAllStatistics(Date fromDate, Date toDate, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.TOTAL_REVENUE);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CustomStatisticDTO> customStatisticDTOS = statisticRepository.getAllStatistics(fromDate, toDate, pageable);

        return customStatisticDTOS.stream()
                .map(dto -> statisticMapper.convertCustomStatisticDTOToStatisticResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticResponse> getAllStatistics(
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
                throw new BadRequestException(ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE, ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE.getMessage());
        }

        if (companyId != null && toiletId == null) {
            return getAllStatisticsByCompanyId(companyId, fromDate, toDate, paginationRequest);
        } else if (companyId == null && toiletId != null) {
            return getAllStatisticsByToiletId(toiletId, fromDate, toDate);
        } else { // List of Total revenue of each companies in System
            return getAllStatistics(fromDate, toDate, paginationRequest);
        }
    }

    private StatisticResponse getTotalStatisticOfMonthByCompanyId(Integer companyId, Date fromDate, Date toDate) {
        // Validate Company ID
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        CustomStatisticDTO customStatisticDTO = statisticRepository.getTotalStatisticOfMonthByCompanyId(companyId, fromDate, toDate);

        return statisticMapper.convertCustomStatisticDTOToStatisticResponse(customStatisticDTO);
    }

    private StatisticResponse getTotalStatisticOfMonthByToiletId(Integer toiletId, Date fromDate, Date toDate) {
        // Validate Toilet ID
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        CustomStatisticDTO customStatisticDTO = statisticRepository.getTotalStatisticOfMonthByToiletId(toiletId, fromDate, toDate);

        return statisticMapper.convertCustomStatisticDTOToStatisticResponse(customStatisticDTO);
    }

    @Override
    public StatisticResponse getTotalStatisticOfMonth(Integer companyId, Integer toiletId) {
        Date toDate = DateTimeUtil.getDateNow();
        Date fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();

        LOGGER.info("ZonedDateTime: " + ZonedDateTime.now(ZoneId.of(DateTimeConstant.ZONE_ID)));
        LOGGER.info("OffsetDateTime: " + OffsetDateTime.now(ZoneOffset.of("+07:00")));
        LOGGER.info("getZoneDateTimeNow: " + DateTimeUtil.getZoneDateTimeNow());
        LOGGER.info("getDateNow: " + toDate + " getFirstDateOfCurrentMonth: " + fromDate);
        LOGGER.info("convertZoneDateTimeToDate123: " + DateTimeUtil.getZoneDateTimeNow().toInstant());
        LOGGER.info("convertZoneDateTimeToDate: " + DateTimeUtil.convertZoneDateTimeToDate(DateTimeUtil.getZoneDateTimeNow()));

        if (companyId != null && toiletId == null) {
            return getTotalStatisticOfMonthByCompanyId(companyId, fromDate, toDate);
        } else if (companyId == null && toiletId != null) {
            return getTotalStatisticOfMonthByToiletId(toiletId, fromDate, toDate);
        } else {
            CustomStatisticDTO customStatisticDTO = statisticRepository.getTotalStatisticOfMonth(fromDate, toDate);
            return statisticMapper.convertCustomStatisticDTOToStatisticResponse(customStatisticDTO);
        }
    }
}

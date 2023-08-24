package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomStatisticDTO;
import com.happy3friends.toiletmapbackend.dto.CustomStatisticForSuggestionDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.StatisticMapper;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.StatisticRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.StatisticForSuggestionResponse;
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

        // Validate Company ID
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        List<CustomStatisticDTO> customStatisticDTOS;
        if (paginationRequest.getPageIndex() == null && paginationRequest.getPageSize() == null) {
            customStatisticDTOS = statisticRepository.getAllStatisticsByCompanyId(companyId, fromDate, toDate, null);
        } else {
            Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);
            customStatisticDTOS = statisticRepository.getAllStatisticsByCompanyId(companyId, fromDate, toDate, pageable);
        }

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

        List<CustomStatisticDTO> customStatisticDTOS;
        if (paginationRequest.getPageIndex() == null && paginationRequest.getPageSize() == null) {
            customStatisticDTOS = statisticRepository.getAllStatistics(fromDate, toDate, null);
        } else {
            Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);
            customStatisticDTOS = statisticRepository.getAllStatistics(fromDate, toDate, pageable);
        }

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
            fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
            toDate = DateTimeUtil.getDateNowWithInitialTime(23, 59, 59, 999);
        } else {  // Validate fromDate & toDate
            fromDate = DateTimeUtil.convertStringToDate(fromStrDate, DateTimeConstant.dd_MM_yyyy);
            toDate = DateTimeUtil.addDays(DateTimeUtil.convertStringToDate(toStrDate, DateTimeConstant.dd_MM_yyyy), 1);
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

    @Override
    public StatisticResponse getTotalStatisticOfMonth(Integer companyId, Integer toiletId) {
        Date fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
        Date toDate = DateTimeUtil.getDateNowWithInitialTime(23, 59, 59, 999);

        CustomStatisticDTO customStatisticDTO = statisticRepository.getTotalStatisticOfMonth(companyId, toiletId, fromDate, toDate);
        return statisticMapper.convertCustomStatisticDTOToStatisticResponse(customStatisticDTO);
    }

    @Override
    public int count(Integer companyId, Integer toiletId, String fromStrDate, String toStrDate) {
        Date fromDate = null;
        Date toDate = null;

        // If fromDate && toDate is null -> Default fromDate and toDate is currentMonth
        if (fromStrDate == null || toStrDate == null) {
            fromDate = DateTimeUtil.getFirstDateOfCurrentMonth();
            toDate = DateTimeUtil.getDateNowWithInitialTime(23, 59, 59, 999);
        } else {  // Validate fromDate & toDate
            fromDate = DateTimeUtil.convertStringToDate(fromStrDate, DateTimeConstant.dd_MM_yyyy);
            toDate = DateTimeUtil.addDays(DateTimeUtil.convertStringToDate(toStrDate, DateTimeConstant.dd_MM_yyyy), 1);
            if (fromDate != null && fromDate.after(toDate))
                throw new BadRequestException(ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE, ToiletMapErrorCodeEnum.FROM_DATE_AFTER_TO_DATE.getMessage());
        }

        if (companyId != null && toiletId == null) {
            return statisticRepository.countAllStatisticsByCompanyId(companyId, fromDate, toDate);
        } else if (companyId == null && toiletId != null) {
            return statisticRepository.countAllStatisticsByToiletId(toiletId, fromDate, toDate);
        } else { // List of Total revenue of each companies in System
            return statisticRepository.countAllStatistics(fromDate, toDate);
        }
    }

    @Override
    public List<StatisticForSuggestionResponse> getStatisticsByToiletId(Integer toiletId, Date fromDate, Date toDate) {
        List<CustomStatisticForSuggestionDTO> customStatisticForSuggestionDTOS = statisticRepository.getStatisticsByToiletId(toiletId ,fromDate, toDate);
        return customStatisticForSuggestionDTOS.stream()
                .map(dto -> statisticMapper.convertCustomStatisticForSuggestionDTOToStatisticForSuggestionResponse(dto))
                .collect(Collectors.toList());
    }
}

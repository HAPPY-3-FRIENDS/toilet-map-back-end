package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.ReportStatusConstant;
import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.dto.CustomReportForManagerDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ReportMapper;
import com.happy3friends.toiletmapbackend.repository.CompanyRepository;
import com.happy3friends.toiletmapbackend.repository.ReportRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.request.CreateReportRequest;
import com.happy3friends.toiletmapbackend.request.UpdateListReportRequest;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponseForManager;
import com.happy3friends.toiletmapbackend.service.ReportService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private ReportResponse getReportResponseFromListCustomReportDTO(
            List<CustomReportDTO> customReportDTOS
    ) {
        ReportResponse response = new ReportResponse();
        response.setToiletName(customReportDTOS.get(0).getToiletName());

        HashMap<String, List<CustomReportDTO>> map = customReportDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomReportDTO::getMessage,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
        Map<String, Map<String, Integer>> test = map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            HashMap<String, List<CustomReportDTO>> map2 = e.getValue().stream()
                                    .collect(Collectors.groupingBy(
                                            CustomReportDTO::getStatus,
                                            LinkedHashMap::new,
                                            Collectors.toCollection(ArrayList::new)));
                            Map<String, Integer> statusAndCount = map2.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            o -> o.getValue().stream().mapToInt(CustomReportDTO::getTotalStatus).sum()
                                    ));

                            return statusAndCount;
                        }
                ));
        response.setMessageAndCount(test);

        return response;
    }

    @Override
    public List<ReportResponse> getReports(Integer companyId, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.TOILET_ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        if (companyId == null)
            return null; //Chua ranh xu ly, hihi

        List<CustomReportDTO> customReportDTOS = reportRepository.getReportsByCompanyId(companyId, pageable);

        HashMap<String, List<CustomReportDTO>> map = customReportDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomReportDTO::getToiletName,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));

        return map.values()
                .stream().map(this::getReportResponseFromListCustomReportDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreateReportResponse createReport(CreateReportRequest request) {
        ReportEntity entity = new ReportEntity();
        entity.setToiletId(request.getToiletId());
        entity.setMessage(request.getMessage());
        entity.setStatus(ReportStatusConstant.NEW);
        entity.setCreateDate(DateTimeUtil.getTimestampNow());

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

    @Override
    public List<ReportResponseForManager> getReportsByToiletIdForManager(int toiletId, List<String> listMessages, List<String> listStatus, BasePaginationRequest paginationRequest) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.TOILET_ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);
        List<CustomReportForManagerDTO> listReport;
        if (listMessages != null && listStatus != null) {
            listReport = reportRepository.getReportsByToiletIdForManagerHasMessageAndStatus(toiletId, listMessages, listStatus, pageable);
        } else if (listMessages == null && listStatus != null) {
            listReport = reportRepository.getReportsByToiletIdForManagerHasStatus(toiletId, listStatus, pageable);
        } else if (listMessages != null) {
            listReport = reportRepository.getReportsByToiletIdForManagerHasMessage(toiletId, listMessages, pageable);
        } else {
            listReport = reportRepository.getReportsByToiletIdForManager(toiletId, pageable);
        }

        return listReport.stream()
                .map(r -> reportMapper.convertCustomReportForManagerDTOToReportResponseForManager(r))
                .collect(Collectors.toList());
    }

    @Override
    public int countReportsByCompanyIdForManager(int id) {
        return reportRepository.countReportsByCompanyIdForManager(id);
    }

    @Override
    public List<CreateReportResponse> updateListReports(UpdateListReportRequest request) {
        String status = request.getStatus();
        List<Integer> listId = request.getListId();
        List<CreateReportResponse> result = new ArrayList<>();
        for (Integer id: listId) {
            Optional<ReportEntity> entity = reportRepository.findById(id);
            if (!entity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_REPORT, ToiletMapErrorCodeEnum.NOT_FOUND_REPORT.getMessage());

            ReportEntity reportEntity = entity.get();
            reportEntity.setStatus(status);
            result.add(reportMapper.convertReportEntitytoCreateReportResponse(reportRepository.save(reportEntity)));
        }
        return result;
    }

    @Override
    public int countReportsByToiletIdForManager(int id, List<String> listMessages, List<String> listStatus) {
        int result = 0;
        if (listMessages != null && listStatus != null) {
            result = reportRepository.countReportsByToiletIdForManagerHasMessageAndStatus(id, listMessages, listStatus);
        } else if (listMessages == null && listStatus != null) {
            result = reportRepository.countReportsByToiletIdForManagerHasStatus(id, listStatus);
        } else if (listMessages != null) {
            result = reportRepository.countReportsByToiletIdForManagerHasMessage(id, listMessages);
        } else {
            result = reportRepository.countReportsByToiletIdForManager(id);
        }
        return result;
    }

    @Override
    public List<ReportResponseForManager> getReportsByCompanyIdForManager(int companyId, BasePaginationRequest paginationRequest) {
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.TOILET_ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CustomReportForManagerDTO> listReport = reportRepository.getReportsByCompanyIdForManager(companyId, pageable);
        return listReport.stream()
                .map(r -> reportMapper.convertCustomReportForManagerDTOToReportResponseForManager(r))
                .collect(Collectors.toList());
    }
}

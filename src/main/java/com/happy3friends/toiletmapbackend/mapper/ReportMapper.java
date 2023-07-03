package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomReportForManagerDTO;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponseForManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReportMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CreateReportResponse convertReportEntitytoCreateReportResponse(ReportEntity entity) {
        return Objects.isNull(entity)
                ? null
                :modelMapper.map(entity, CreateReportResponse.class);
    }

    public ReportResponseForManager convertCustomReportForManagerDTOToReportResponseForManager(CustomReportForManagerDTO customReportForManagerDTO) {
        return Objects.isNull(customReportForManagerDTO)
                ? null
                :modelMapper.map(customReportForManagerDTO, ReportResponseForManager.class);
    }
}

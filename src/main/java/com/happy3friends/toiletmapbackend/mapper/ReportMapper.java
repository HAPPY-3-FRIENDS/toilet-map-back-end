package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReportMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ReportResponse convertCustomReportDTOToReportResponse(CustomReportDTO customReportDTO) {
        return Objects.isNull(customReportDTO)
                ? null
                : modelMapper.map(customReportDTO, ReportResponse.class);
    }
}

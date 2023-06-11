package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
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
}
